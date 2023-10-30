package com.byt3.byaudio.controller.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.byt3.byaudio.R;
import com.byt3.byaudio.utils.functions;

public class PlayerFragment extends Fragment {
    ImageView albumCover;
    TextView songName, artistName, currentTime, totalDuration;
    SeekBar seekBar;
    ImageButton songDetail, pausePlay, forward, rewind, nextSong, previousSong;
    MediaPlayer player;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        initView(view);
        Context context = getContext();

        player = MediaPlayer.create(context, R.raw.lemminofirecracker);
        player.setLooping(true);
        currentTime.post(mUpdateTime);
        totalDuration.setText(player.getDuration());

        pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.isPlaying())
                    player.pause();
                else
                    player.start();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.getCurrentPosition();
            }
        });

        seekBar.setMax(player.getDuration());
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                UpdateseekChange(v);
                return false;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        player.release();
    }

    private void initView(View view) {
        albumCover = view.findViewById(R.id.albumCover);
        songName = view.findViewById(R.id.songName);
        artistName = view.findViewById(R.id.artistName);
        currentTime = view.findViewById(R.id.currentTime);
        totalDuration = view.findViewById(R.id.totalTime);
        seekBar = view.findViewById(R.id.slider);
        songDetail = view.findViewById(R.id.songDetail);
        pausePlay = view.findViewById(R.id.pausePlay);
        forward = view.findViewById(R.id.fastForward);
        rewind = view.findViewById(R.id.fastRewind);
        nextSong = view.findViewById(R.id.nextSong);
        previousSong = view.findViewById(R.id.previousSong);
    }

    private Runnable mUpdateTime = new Runnable() {
        public void run() {
            int currentDuration;
            if (player.isPlaying()) {
                currentDuration = player.getCurrentPosition();
                updatePlayer(currentDuration);
                currentTime.postDelayed(this, 1000);
            }else {
                currentTime.removeCallbacks(this);
            }
        }
    };
    private void updatePlayer(int currentDuration){
        String s = "" + functions.milliSecondsToTimer((long) currentDuration);
        currentTime.setText(s);
    }

    private void UpdateseekChange(View v){
        if(player.isPlaying()){
            SeekBar sb = (SeekBar)v;
            player.seekTo(sb.getProgress());
        }
    }
}
