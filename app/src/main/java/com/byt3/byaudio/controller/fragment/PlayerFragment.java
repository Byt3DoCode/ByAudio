package com.byt3.byaudio.controller.fragment;

import static com.byt3.byaudio.utils.functions.MY_LOGCAT;
import static com.byt3.byaudio.utils.functions.cleanName;
import static com.byt3.byaudio.utils.functions.getBitmapFromPath;
import static com.byt3.byaudio.utils.functions.milliSecondsToTimer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.SongDetailActivity;
import com.byt3.byaudio.controller.service.PlayerService;
import com.byt3.byaudio.model.Song;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class PlayerFragment extends Fragment {
    ImageView albumCover;
    TextView songName, artistName, currentTime, totalDuration;
    SeekBar seekBar;
    ImageButton songDetail, pausePlay, forward, rewind, nextSong, previousSong, repeatMode;
    Context context;
    Song currentSong;
    Observer<Song> songObserver = new Observer<Song>() {
        @Override
        public void onChanged(Song song) {
            currentSong = song;
            handleLayout(currentSong);
            startPlayProgressUpdater();
        }
    };
    Observer<Boolean> isPlayingObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean b) {
            if (b) {
                pausePlay.setImageResource(R.drawable.ic_pause_24);
                startPlayProgressUpdater();
            } else {
                pausePlay.setImageResource(R.drawable.ic_play_24);
            }
        }
    };
    PlayerService playerService;
    boolean isServiceConnected;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayerService.MyBinder binder = (PlayerService.MyBinder) iBinder;
            playerService = binder.getplayerService();
            playerService.songLiveData.observe(PlayerFragment.this, songObserver);
            playerService.isPlayingLiveData.observe(PlayerFragment.this, isPlayingObserver);
            if (playerService.hasStarted()){
                currentSong = playerService.getSong();
                handleLayout(currentSong);
                startPlayProgressUpdater();
            } else
                playSongFromMemory();
            isServiceConnected = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceConnected = false;
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("song", "");
        currentSong = new Gson().fromJson(json, Song.class);
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
        songDetail.setOnClickListener(view16 -> {
            if (currentSong != null) {
                Intent intent = new Intent(context, SongDetailActivity.class);
                intent.putExtra("song", (Parcelable) currentSong);
                context.startActivity(intent);
            }
        });
        repeatMode.setOnClickListener(view17 -> {
            Log.d(MY_LOGCAT, String.valueOf(playerService.getRepeatMode()));
            switch (playerService.getRepeatMode()){
                case PlayerService.REPEAT_OFF:
                    playerService.setRepeatMode(PlayerService.REPEAT_ONE);
                    repeatMode.setImageResource(R.drawable.ic_repeat_one_on_24);
                    break;
                case PlayerService.REPEAT_ONE:
                    playerService.setRepeatMode(PlayerService.REPEAT_ALL);
                    repeatMode.setImageResource(R.drawable.ic_repeat_on_24);
                    break;
                case PlayerService.REPEAT_ALL:
                    playerService.setRepeatMode(PlayerService.REPEAT_OFF);
                    repeatMode.setImageResource(R.drawable.ic_repeat_24);
                    break;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(playerService.getMediaPlayer() != null && b)
                    playerService.getMediaPlayer().seekTo(i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(playerService.getMediaPlayer() != null)
                    playerService.getMediaPlayer().seekTo(seekBar.getProgress());
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
        pausePlay = view.findViewById(R.id.pausePlay);
        forward = view.findViewById(R.id.fastForward);
        rewind = view.findViewById(R.id.fastRewind);
        nextSong = view.findViewById(R.id.nextSong);
        previousSong = view.findViewById(R.id.previousSong);
        repeatMode = view.findViewById(R.id.repeatSetting);
    }

    private void handleLayout(Song song) {
        currentTime.setText(milliSecondsToTimer(0));
        songName.setText(cleanName(song.getName()));
        songName.setSelected(true);
        artistName.setText(song.getArtist().getName());
        totalDuration.setText(milliSecondsToTimer(song.getDuration()* 1000L));
        seekBar.setMax(song.getDuration() * 1000);
        if (song.getAlbum().getImage() == 1){
            Bitmap bm = getBitmapFromPath(song);
            albumCover.setImageBitmap(bm);
        } else if (song.getAlbum().getImage() == 2) {
            Picasso.get().load(song.getAlbum().getUrl()).into(albumCover);
        } else
            albumCover.setImageResource(R.drawable.ic_cat);
        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("Song", new Gson().toJson(currentSong)).apply();
    }

    private void startPlayProgressUpdater() {
        long currentDuration = playerService.getMediaPlayer().getCurrentPosition();
        seekBar.setProgress((int) currentDuration);
        String s = milliSecondsToTimer(currentDuration);
        currentTime.setText(s);
        if (playerService.getMediaPlayer().isPlaying()) {
            Runnable notification = this::startPlayProgressUpdater;
            seekBar.postDelayed(notification,1000);
        }
    }

    private void playSongFromMemory(){
        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("song", "");
        currentSong = new Gson().fromJson(json, Song.class);
    }
}
