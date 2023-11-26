package com.byt3.byaudio.controller.adapter;

import static com.byt3.byaudio.utils.functions.getBitmapFromPath;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.FolderDetailActivity;
import com.byt3.byaudio.controller.fragment.PlayerFragment;
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
    private String queryText, folderName;
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

    public void setFolderName(String folderName){
        this.folderName = folderName;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song obj = list.get(position);
        assert obj != null : "object is null";
        holder.songName.setText(functions.cleanName(obj.getName()));
        holder.artistName.setText(obj.getArtist().getName());
        holder.songLength.setText(functions.milliSecondsToTimer(obj.getDuration()* 1000L));
        if (obj.getAlbum().getImage() == 0) {
            holder.albumCover.setImageResource(R.drawable.ic_cat);
            holder.albumCover.setBackgroundColor(context.getColor(R.color.mint));
        } else {
            holder.albumCover.setImageBitmap(getBitmapFromPath(obj));
            holder.albumCover.setBackgroundColor(context.getColor(R.color.gray));
        }
        holder.itemView.setOnClickListener(view -> {
            String name;
            if (context instanceof FolderDetailActivity)
                name = "Folder"+folderName;
            else
                name = "Search"+queryText;
            long totalDuration = 0;
            for (Song s : list) {
                totalDuration += s.getDuration();
            }
            SongCollection songCollection = new SongCollection(name,
                    SongCollection.TYPE_QUEUE,
                    list.size(),
                    totalDuration);
            songCollection.setScId(Math.toIntExact(db.songCollectionDAO().insert(songCollection)));
            for (Song s : list) {
                CollectionSongCrossRef crossRef = new CollectionSongCrossRef(
                        songCollection.getScId(),
                        s.getSongId());
                db.songCollectionDAO().insertCollectionSongCrossRef(crossRef);
            }

            ViewPager2 viewPager = activity.findViewById(R.id.viewpager);
            viewPager.setCurrentItem(1, false);

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("songs", new ArrayList<>(list));
            bundle.putInt("index", holder.getAdapterPosition());

            PlayerFragment fragment = new PlayerFragment();
            fragment.setArguments(bundle);
            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.playerFragment, fragment)
                    .commit();
        });
        holder.optionButton.setVisibility(View.GONE);
//        holder.optionButton.setOnClickListener(view -> {
//            Intent intent=new Intent(context, SongDetailActivity.class);
//            intent.putExtra("object",obj);
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }
}
