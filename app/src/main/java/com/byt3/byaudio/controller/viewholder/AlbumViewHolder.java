package com.byt3.byaudio.controller.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;

public class AlbumViewHolder extends RecyclerView.ViewHolder{
    public ImageView imageView;
    public AlbumViewHolder(@NonNull View view) {
        super(view);
        imageView = view.findViewById(R.id.imageView);
    }
}
