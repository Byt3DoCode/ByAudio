package com.byt3.byaudio.controller.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;

public class PlaylistViewHolder extends RecyclerView.ViewHolder{
    public TextView plName, plSize, plDuration;

    public PlaylistViewHolder(@NonNull View view) {
        super(view);
        plName = view.findViewById(R.id.playlistName);
        plSize = view.findViewById(R.id.playlistSize);
        plDuration = view.findViewById(R.id.playlistDuration);
    }
}
