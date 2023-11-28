package com.byt3.byaudio.controller;

import static com.byt3.byaudio.utils.functions.getBitmapFromPath;
import static com.byt3.byaudio.utils.functions.milliSecondsToTimer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.byt3.byaudio.R;
import com.byt3.byaudio.model.Song;

import java.util.Objects;

public class SongDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView albumCover;
    TextView songName, artistName, albumName, songDuration, songPath;
    Button doneBtn;
    Song song;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);
        toolbar = findViewById(R.id.toolBar);
        albumCover = findViewById(R.id.albumCover);
        songName = findViewById(R.id.songName);
        artistName = findViewById(R.id.artistName);
        albumName = findViewById(R.id.albumName);
        songDuration = findViewById(R.id.songDuration);
        songPath = findViewById(R.id.songPath);
        doneBtn = findViewById(R.id.doneBtn);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        song = getIntent().getParcelableExtra("song");

        assert song != null;
        if (song.getAlbum().getImage() == 1) {
            Bitmap bitmap = getBitmapFromPath(song);
            albumCover.setImageBitmap(bitmap);
        }
        songName.setText(song.getName());
        artistName.setText(song.getArtist().getName());
        albumName.setText(song.getAlbum().getName());
        songDuration.setText(milliSecondsToTimer(song.getDuration() * 1000L));
        songPath.setText(song.getFolder().getPath());

        doneBtn.setOnClickListener(view -> finish());
    }
}
