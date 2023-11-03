package com.byt3.byaudio.controller.fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
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
    public static final String CHANNEL_ID = "1001";
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
        player.start();
        player.setLooping(true);
        currentTime.post(mUpdateTime);
        totalDuration.setText(functions.milliSecondsToTimer(player.getDuration()));

        pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player == null)
                    player = MediaPlayer.create(context, R.raw.lemminofirecracker);
                if(player.isPlaying()){
                    player.pause();
                    pausePlay.setImageResource(R.drawable.icon_play_arrow_64);
                } else{
                    player.start();
                    pausePlay.setImageResource(R.drawable.icon_pause_64);
                }
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.getCurrentPosition();
            }
        });
        if (player != null){
            seekBar.setMax(player.getDuration());
            startPlayProgressUpdater();
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(player != null && b){
                    player.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null)
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

    public void startPlayProgressUpdater() {
        seekBar.setProgress(player.getCurrentPosition());

        if (player.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();
                }
            };
            seekBar.postDelayed(notification,1000);
        }
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,"Player Service", NotificationManager.IMPORTANCE_DEFAULT);
            Context context = getContext();
            if(context != null){
                NotificationManager manager = context.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
