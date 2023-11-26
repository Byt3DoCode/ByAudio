package com.byt3.byaudio.controller.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;

public class DraggableSongViewHolder extends RecyclerView.ViewHolder{
    public TextView songName, artistName, songLength;

    public DraggableSongViewHolder(@NonNull View view) {
        super(view);
        songName = view.findViewById(R.id.songName);
        artistName = view.findViewById(R.id.artistName);
        songLength = view.findViewById(R.id.songLength);
    }
}
