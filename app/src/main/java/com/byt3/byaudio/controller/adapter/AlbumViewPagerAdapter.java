package com.byt3.byaudio.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.viewholder.AlbumViewHolder;
import com.byt3.byaudio.model.Album;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumViewPagerAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
    private final Context context;
    private List<Album> list;

    public AlbumViewPagerAdapter(Context context, List<Album> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<Album> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = list.get(position);
        Picasso.get().load(album.getUrl()).into(holder.imageView);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }
}
