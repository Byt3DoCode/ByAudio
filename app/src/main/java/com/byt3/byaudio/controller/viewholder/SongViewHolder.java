package com.byt3.byaudio.controller.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;

public class SongViewHolder extends RecyclerView.ViewHolder{
    public ImageView albumCover;
    public TextView songName, artistName, songLength;
    public ImageButton optionButton;

    public SongViewHolder(@NonNull View view) {
        super(view);
        albumCover = view.findViewById(R.id.albumCover);
        songName = view.findViewById(R.id.songName);
        artistName = view.findViewById(R.id.artistName);
        songLength = view.findViewById(R.id.songLength);
        optionButton = view.findViewById(R.id.buttonOption);
    }
}
