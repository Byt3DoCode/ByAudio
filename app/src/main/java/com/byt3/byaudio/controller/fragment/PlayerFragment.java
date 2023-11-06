package com.byt3.byaudio.controller.fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.service.PlayerService;
import com.byt3.byaudio.utils.functions;

import java.io.IOException;

public class PlayerFragment extends Fragment {
    public static final String CHANNEL_ID = "1001";
    ImageView albumCover;
    TextView songName, artistName, currentTime, totalDuration;
    SeekBar seekBar;
    ImageButton songDetail, pausePlay, forward, rewind, nextSong, previousSong;
    MediaPlayer player;
    Context context;
    Intent intent;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
//        createNotificationChannel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        initView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                displaySongInfo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

//        player = MediaPlayer.create(context, R.raw.lemminofirecracker);
//        player.start();
//        player.setLooping(true);
//        currentTime.post(mUpdateTime);
//        totalDuration.setText(functions.milliSecondsToTimer(player.getDuration()));

//        intent = new Intent(PlayerService.START);
//        context.startService(intent);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null){
            player.release();
            player = null;
        }
//        context.stopService(intent);
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
            serviceChannel.setName("ByAudio");
            Context context = getContext();
            if(context != null){
                NotificationManager manager = context.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void displaySongInfo() throws IOException {
        try (MediaMetadataRetriever mmr = new MediaMetadataRetriever()) {
            Uri uri = new Uri.Builder()
                    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                    .authority(context.getPackageName())
                    .appendPath(String.valueOf(R.raw.lemminofirecracker))
                    .build();
            mmr.setDataSource(context, uri);

            String temporary;
            temporary = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (temporary != null)
                songName.setText(temporary);
            else
                songName.setText("test");

            temporary = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            if (temporary != null)
                artistName.setText(temporary);
            else
                artistName.setText("Unknown");

            totalDuration.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

            byte[] artBytes = mmr.getEmbeddedPicture();
            if (artBytes!=null){
                Bitmap bm = BitmapFactory.decodeByteArray(artBytes,0,artBytes.length);
                albumCover.setImageBitmap(bm);
            }
        }
    }
}
