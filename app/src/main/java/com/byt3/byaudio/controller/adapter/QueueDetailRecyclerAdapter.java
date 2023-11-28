package com.byt3.byaudio.controller.adapter;

import static com.byt3.byaudio.utils.functions.cleanName;
import static com.byt3.byaudio.utils.functions.milliSecondsToTimer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.service.PlayerService;
import com.byt3.byaudio.controller.viewholder.DraggableSongViewHolder;
import com.byt3.byaudio.model.AppDatabase;
import com.byt3.byaudio.model.Song;

import java.util.ArrayList;
import java.util.List;

public class QueueDetailRecyclerAdapter extends RecyclerView.Adapter<DraggableSongViewHolder> {
    private final Context context;
    private final Activity activity;
    private List<Song> list;
    private int index;
    private final AppDatabase db;

    public QueueDetailRecyclerAdapter(Context context, List<Song> list, Context activityContext) {
        this.context = context;
        this.list = list;
        this.activity = (Activity) activityContext;
        this.db = AppDatabase.getInstance(context);
    }

    public void setList(List<Song> list){
        this.list = list;
        notifyDataSetChanged();
    }
    public void setIndex(int index){
        int oldIndex = this.index;
        this.index = index;
        notifyItemChanged(oldIndex);
        notifyItemChanged(index);
    }
    public void setListSilent(List<Song> list){
        this.list = list;
    }

    @NonNull
    @Override
    public DraggableSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_draggable_song, parent, false);
        return new DraggableSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DraggableSongViewHolder holder, int position) {
        Song obj = list.get(position);
        assert obj != null;
        if (position == index){
            holder.songName.setTextColor(context.getColor(R.color.mint));
            holder.artistName.setTextColor(context.getColor(R.color.lighterMint));
        } else {
            holder.songName.setTextColor(context.getColor(R.color.white));
            holder.artistName.setTextColor(context.getColor(R.color.white));
        }
        holder.songName.setText(cleanName(obj.getName()));
        holder.artistName.setText(obj.getArtist().getName());
        holder.songLength.setText(milliSecondsToTimer(obj.getDuration()* 1000L));
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlayerService.class);
            intent.putExtra("newList", true);
            intent.putExtra("index", holder.getAdapterPosition());
            intent.putParcelableArrayListExtra("songs", new ArrayList<>(list));
            context.startService(intent);
            setIndex(holder.getAdapterPosition());
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
