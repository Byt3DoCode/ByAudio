package com.byt3.byaudio.controller.fragment;

import static com.byt3.byaudio.utils.functions.MY_LOGCAT;
import static com.byt3.byaudio.utils.functions.cleanName;
import static com.byt3.byaudio.utils.functions.getBitmapFromPath;
import static com.byt3.byaudio.utils.functions.milliSecondsToTimer;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
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
import com.byt3.byaudio.controller.MainActivity;
import com.byt3.byaudio.controller.service.PlayerService;
import com.byt3.byaudio.model.AppDatabase;
import com.byt3.byaudio.model.Song;
import com.byt3.byaudio.utils.functions;

import java.io.IOException;
import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class PlayerFragment extends Fragment {
    ImageView albumCover;
    TextView songName, artistName, currentTime, totalDuration;
    SeekBar seekBar;
    ImageButton songDetail, pausePlay, forward, rewind, nextSong, previousSong;
    Context context;
    Song currentSong;
    PlayerService playerService;
    boolean isServiceConnected;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(MY_LOGCAT,"in service connected ");
            PlayerService.MyBinder binder = (PlayerService.MyBinder) iBinder;
            playerService = binder.getplayerService();
            if (playerService.hasStarted()){
                currentSong = playerService.getSong();
                handleLayout(currentSong);
                startPlayProgressUpdater();
            }
            isServiceConnected = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(MY_LOGCAT,"in service disconnected");
            isServiceConnected = false;
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        initView(view);

        Intent serviceIntent = new Intent(context, PlayerService.class);
        context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        pausePlay.setOnClickListener(view15 -> {
            if (playerService.getMediaPlayer() != null){
                if (playerService.getMediaPlayer().isPlaying()) {
                    pausePlay.setImageResource(R.drawable.ic_play_24);
                    playerService.pauseAudio();
                } else {
                    pausePlay.setImageResource(R.drawable.ic_pause_24);
                    playerService.resumeAudio();
                    startPlayProgressUpdater();
                }
            }
        });
        nextSong.setOnClickListener(view14 -> {
            if (playerService.getMediaPlayer() != null){
                playerService.playNextSong();
                handleLayout(playerService.getSong());
            }
        });
        previousSong.setOnClickListener(view13 -> {
            if (playerService.getMediaPlayer() != null){
                playerService.playPreviousSong();
                handleLayout(playerService.getSong());
            }
        });
        forward.setOnClickListener(view12 -> {
            if (playerService.getMediaPlayer() != null)
                playerService.fastForward();
        });
        rewind.setOnClickListener(view1 -> {
            if (playerService.getMediaPlayer() != null)
                playerService.rewind();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(playerService.getMediaPlayer() != null && b){
                    playerService.getMediaPlayer().seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(playerService.getMediaPlayer() != null){
                    playerService.getMediaPlayer().seekTo(seekBar.getProgress());
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(context, PlayerService.class);
        context.stopService(serviceIntent);
        if (isServiceConnected){
            context.unbindService(serviceConnection);
            isServiceConnected = false;
        }
    }

    private void initView(View view) {
        albumCover = view.findViewById(R.id.albumCover);
        songName = view.findViewById(R.id.songName);
        artistName = view.findViewById(R.id.artistName);
        currentTime = view.findViewById(R.id.currentTime);
        totalDuration = view.findViewById(R.id.totalTime);
        seekBar = view.findViewById(R.id.slider);
        songDetail = view.findViewById(R.id.songDetail);
        songDetail.setVisibility(View.INVISIBLE);
        pausePlay = view.findViewById(R.id.pausePlay);
        forward = view.findViewById(R.id.fastForward);
        rewind = view.findViewById(R.id.fastRewind);
        nextSong = view.findViewById(R.id.nextSong);
        previousSong = view.findViewById(R.id.previousSong);
    }

    private void handleLayout(Song song) {
        songName.setText(cleanName(song.getName()));
        artistName.setText(song.getArtist().getName());
        totalDuration.setText(milliSecondsToTimer(song.getDuration()* 1000L));
        seekBar.setMax(song.getDuration() * 1000);
        if (song.getAlbum().getImage() == 1){
            Bitmap bm = getBitmapFromPath(song);
            albumCover.setImageBitmap(bm);
        } else
            albumCover.setImageResource(R.drawable.ic_cat);
    }

    public void startPlayProgressUpdater() {
        if (playerService.getSong() != currentSong)
            handleLayout(playerService.getSong());
        long currentDuration = playerService.getMediaPlayer().getCurrentPosition();
        seekBar.setProgress((int) currentDuration);
        String s = milliSecondsToTimer(currentDuration);
        currentTime.setText(s);
        if (playerService.getMediaPlayer().isPlaying()) {
            Runnable notification = this::startPlayProgressUpdater;
            seekBar.postDelayed(notification,1000);
        }
    }
}
