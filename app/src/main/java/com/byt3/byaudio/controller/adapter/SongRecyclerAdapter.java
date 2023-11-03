package com.byt3.byaudio.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.viewholder.SongViewHolder;
import com.byt3.byaudio.model.Song;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongRecyclerAdapter extends RecyclerView.Adapter<SongViewHolder> {
    private Context context;
    private List<Song> list;

    public SongRecyclerAdapter(Context context, List<Song> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<Song> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song s = list.get(position);
//        if (s.getImage() == null || s.getImage().equals("\"\"") || s.getImage().equals("")) {
//            holder.albumCover.setImageResource(R.drawable.ic_cat);
//        } else {
//            Picasso.get().load(s.getImage()).into(holder.albumCover);
//        }
        holder.songName.setText(s.getName());
        holder.artistName.setText(s.getArtist());
        holder.songLength.setText(s.getDuration());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(context, SongDetailActivity.class);
//                intent.putExtra("song_object",s);
//                context.startActivity(intent);
            }
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
