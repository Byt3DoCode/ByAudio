package com.byt3.byaudio.controller.adapter;

import static com.byt3.byaudio.utils.functions.getBitmapFromPath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.FolderDetailActivity;
import com.byt3.byaudio.controller.SongDetailActivity;
import com.byt3.byaudio.controller.service.PlayerService;
import com.byt3.byaudio.controller.viewholder.SongViewHolder;
import com.byt3.byaudio.model.AppDatabase;
import com.byt3.byaudio.model.CollectionSongCrossRef;
import com.byt3.byaudio.model.Song;
import com.byt3.byaudio.model.SongCollection;
import com.byt3.byaudio.utils.functions;

import java.util.ArrayList;
import java.util.List;

public class SongRecyclerAdapter extends RecyclerView.Adapter<SongViewHolder> {
    private final Context context;
    private final Activity activity;
    private List<Song> list;
    private String queryText, listName;
    private final AppDatabase db;

    public SongRecyclerAdapter(Context context, List<Song> list, Context activityContext) {
        this.context = context;
        this.list = list;
        this.activity = (Activity) activityContext;
        this.db = AppDatabase.getInstance(context);
    }

    public void setList(List<Song> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void setQueryText(String queryText){
        this.queryText = queryText;
    }

    public void setListName(String listName){
        this.listName = listName;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = list.get(position);
        assert song != null : "object is null";
        holder.songName.setText(functions.cleanName(song.getName()));
        holder.artistName.setText(song.getArtist().getName());
        holder.songLength.setText(functions.milliSecondsToTimer(song.getDuration()* 1000L));
        if (song.getAlbum().getImage() == 0) {
            holder.albumCover.setImageResource(R.drawable.ic_cat);
            holder.albumCover.setBackgroundColor(context.getColor(R.color.mint));
        } else {
            holder.albumCover.setImageBitmap(getBitmapFromPath(song));
            holder.albumCover.setBackgroundColor(context.getColor(R.color.gray));
        }
        holder.itemView.setOnClickListener(view -> {
            String name = "Queue ";
            long totalDuration = 0;
            int count = 0;
            if (context instanceof FolderDetailActivity)
                name += listName;
            else
                name += "Search " + queryText;
            SongCollection songCollection = new SongCollection(name,
                    SongCollection.TYPE_QUEUE,
                    list.size(),
                    totalDuration);
            songCollection.setScId(Math.toIntExact(db.songCollectionDAO().insert(songCollection)));
            for (Song s : list) {
                totalDuration += s.getDuration();
                CollectionSongCrossRef crossRef = new CollectionSongCrossRef(
                        songCollection.getScId(),
                        s.getSongId(),
                        count++);
                db.CoSosDAO().insertCollectionSongCrossRef(crossRef);
            }
            songCollection.setScTotalDuration(totalDuration);
            db.songCollectionDAO().updateCollection(songCollection);

            Intent intent = new Intent(context, PlayerService.class);
            intent.putExtra("newList", true);
            intent.putParcelableArrayListExtra("songs", new ArrayList<>(list));
            intent.putExtra("index", holder.getAdapterPosition());
            context.startService(intent);
            if (context instanceof FolderDetailActivity)
                ((FolderDetailActivity) context).finish();
        });
        holder.optionButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, SongDetailActivity.class);
            intent.putExtra("song",song);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }
}
