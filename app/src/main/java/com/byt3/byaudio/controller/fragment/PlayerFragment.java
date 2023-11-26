package com.byt3.byaudio.controller.fragment;

import static com.byt3.byaudio.utils.functions.cleanName;
import static com.byt3.byaudio.utils.functions.getBitmapFromPath;
import static com.byt3.byaudio.utils.functions.milliSecondsToTimer;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.byt3.byaudio.model.Song;
import com.byt3.byaudio.utils.functions;

import java.io.IOException;
import java.util.Objects;

public class PlayerFragment extends Fragment {
    ImageView albumCover;
    TextView songName, artistName, currentTime, totalDuration;
    SeekBar seekBar;
    ImageButton songDetail, pausePlay, forward, rewind, nextSong, previousSong;
    MediaPlayer player;
    Context context;
    Intent intent;
    private Song song;
    boolean isPlaying;

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

        Bundle bundle = getArguments();
        if (bundle != null){
//            song = (Song) Objects.requireNonNull(bundle.getParcelableArrayList("songs")).get(bundle.getInt("index"));
            isPlaying = true;
            intent = new Intent(context,PlayerService.class);
//            seekBar.setMax(song.getDuration()*1000);
//            startPlayProgressUpdater();
            intent.putExtra("newList", true);
            intent.putExtra("index", bundle.getInt("index"));
            intent.putExtra("songs", bundle.getParcelableArrayList("songs"));
            context.startService(intent);
        }

//        try {
//            displaySongInfo();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

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
                if (isPlaying) {
                    isPlaying = false;
                    pausePlay.setImageResource(R.drawable.ic_play_24);
                    intent = new Intent(context,PlayerService.class);
                    intent.putExtra("actionToService", PlayerService.PAUSE);
                    context.startService(intent);
                } else {
                    isPlaying = true;
                    pausePlay.setImageResource(R.drawable.ic_pause_24);
                    intent = new Intent(context,PlayerService.class);
                    intent.putExtra("actionToService", PlayerService.RESUME);
                    context.startService(intent);
                }
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.getCurrentPosition();
            }
        });

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

    private final Runnable mUpdateTime = new Runnable() {
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
        String s = "" + milliSecondsToTimer((long) currentDuration);
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

    private void displaySongInfo() throws IOException {
        songName.setText(cleanName(song.getName()));
        artistName.setText(song.getArtist().getName());
        totalDuration.setText(milliSecondsToTimer(song.getDuration()* 1000L));
        if (song.getAlbum().getImage() == 1){
            Bitmap bm = getBitmapFromPath(song);
            albumCover.setImageBitmap(bm);
        }
    }
}
