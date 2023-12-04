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
import com.byt3.byaudio.model.objrelation.CollectionWithSongs;

import java.util.ArrayList;
import java.util.List;

public class QueueRecyclerAdapter extends RecyclerView.Adapter<DraggableSongViewHolder> {
    private final Context context;
    private final Activity activity;
    private List<CollectionWithSongs> list;
    private int index;
    private final AppDatabase db;

    public QueueRecyclerAdapter(Context context, List<CollectionWithSongs> list, Context activityContext) {
        this.context = context;
        this.list = list;
        this.activity = (Activity) activityContext;
        this.db = AppDatabase.getInstance(context);
    }

    public void setList(List<CollectionWithSongs> list){
        this.list = list;
        notifyDataSetChanged();
    }
    public int getIndex(){
        return index;
    }

    @NonNull
    @Override
    public DraggableSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_draggable_song, parent, false);
        return new DraggableSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DraggableSongViewHolder holder, int position) {
        CollectionWithSongs obj = list.get(position);
        assert obj != null;
        holder.songName.setText(cleanName(obj.songCollection.getScName()));
        holder.artistName.setVisibility(View.GONE);
        holder.songLength.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(view -> index = holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }
}
