package com.byt3.byaudio.controller.adapter;

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
import com.byt3.byaudio.controller.FolderDetailActivity;
import com.byt3.byaudio.controller.viewholder.PlaylistViewHolder;
import com.byt3.byaudio.model.SongCollection;

import java.util.List;

public class PlaylistRecyclerAdapter extends RecyclerView.Adapter<PlaylistViewHolder>{
    private final Context context;
    private final Activity activity;
    private List<SongCollection> list;

    public PlaylistRecyclerAdapter(Context context, List<SongCollection> list, Context activityContext) {
        this.context = context;
        this.list = list;
        this.activity = (Activity) activityContext;
    }

    public void setList(List<SongCollection> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        SongCollection playlist = list.get(position);
        holder.plName.setText(playlist.getScName());
        String size = playlist.getScSize() + " songs";
        holder.plSize.setText(size);
        holder.plDuration.setText(milliSecondsToTimer(playlist.getScTotalDuration() * 1000L));
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, FolderDetailActivity.class);
            intent.putExtra("playlist", playlist);
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
