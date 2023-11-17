package com.byt3.byaudio.controller.adapter;

import static com.byt3.byaudio.utils.functions.getBitmapFromPath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.SongDetailActivity;
import com.byt3.byaudio.controller.fragment.PlayerFragment;
import com.byt3.byaudio.controller.viewholder.SongViewHolder;
import com.byt3.byaudio.model.Song;
import com.byt3.byaudio.utils.functions;

import java.io.Serializable;
import java.util.List;

public class SongRecyclerAdapter extends RecyclerView.Adapter<SongViewHolder> {
    private final Context context;
    private final Activity activity;
    private List<Song> list;

    public SongRecyclerAdapter(Context context, List<Song> list, Context activityContext) {
        this.context = context;
        this.list = list;
        this.activity = (Activity) activityContext;
        notifyDataSetChanged();
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
        Song obj = list.get(position);
        assert obj != null : "object is null";
        if (obj.getAlbum().getImage() == 0) {
            holder.albumCover.setImageResource(R.drawable.ic_cat);
            holder.albumCover.setBackgroundColor(context.getColor(R.color.mint));
        } else {
            holder.albumCover.setImageBitmap(getBitmapFromPath(obj.getFolder().getPath() + "/" + obj.getName()));
            holder.albumCover.setBackgroundColor(context.getColor(R.color.gray));
        }
        holder.songName.setText(functions.cleanName(obj.getName()));
        holder.artistName.setText(obj.getArtist().getName());
        holder.songLength.setText(functions.milliSecondsToTimer(obj.getDuration()* 1000L));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewPager2 viewPager = activity.findViewById(R.id.viewpager);
                viewPager.setCurrentItem(1);

                Bundle bundle = new Bundle();
                bundle.putSerializable("song", obj);

                PlayerFragment fragment = new PlayerFragment();
                fragment.setArguments(bundle);
            }
        });
        holder.optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SongDetailActivity.class);
                intent.putExtra("object",obj);
                context.startActivity(intent);
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
