package com.byt3.byaudio.controller.service;

import static com.byt3.byaudio.utils.functions.CHANNEL_ID;
import static com.byt3.byaudio.utils.functions.buildFullPath;
import static com.byt3.byaudio.utils.functions.getBitmapFromPath;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.MainActivity;
import com.byt3.byaudio.model.Song;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener{
    private ArrayList<Song> list;
    private int index;
    private final boolean loopQueue = false;
    private MediaPlayer mediaPlayer = null;

    public static final int PLAY = 0;
    public static final int PAUSE = 2;
    public static final int RESUME = 3;
    public static final int NEXT = 4;
    public static final int PREVIOUS = 5;
    public static final int FORWARD = 6;
    public static final int REWIND = 7;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (list == null || intent.getBooleanExtra("newList", false)){
            list = intent.getParcelableArrayListExtra("songs");
            index = intent.getIntExtra("index", 0);
        }
        int action = intent.getIntExtra("actionToService", 0);
        handleAction(action);
        return START_NOT_STICKY;
    }

    private void handleAction(int action) {
        switch (action){
            case PLAY:
                playAudio();
                break;
            case PAUSE:
                pauseAudio();
                break;
            case RESUME:
                resumeAudio();
                break;
            case NEXT:
                playNextSong();
                break;
            case PREVIOUS:
                playPreviousSong();
                break;
            case FORWARD:
                fastForward();
                break;
            case REWIND:
                rewind();
                break;
        }
    }

    private void playAudio() {
        if (mediaPlayer == null) {
            Uri uri = new Uri.Builder()
                    .path(buildFullPath(list.get(index)))
                    .build();
            mediaPlayer = MediaPlayer.create(this, uri);
        } else {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(buildFullPath(list.get(index)));
                mediaPlayer.prepare();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        mediaPlayer.start();
        setupNotification(list.get(index));
    }

    private void pauseAudio(){
        mediaPlayer.pause();
        setupNotification(list.get(index));
    }

    private void resumeAudio(){
        mediaPlayer.start();
        setupNotification(list.get(index));
    }

    private void playNextSong(){
        if (index < list.size() - 1)
            ++index;
        else if (loopQueue)
            index = 0;
        playAudio();
    }

    private void playPreviousSong(){
        if (index > 0)
            --index;
        else if (loopQueue)
            index = list.size() - 1;
        playAudio();
    }

    private void fastForward(){
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000 ,MediaPlayer.SEEK_NEXT_SYNC);
    }

    private void rewind(){

    }

    private void setLoopSong(){

    }

    private void setLoopQueue(){

    }

    private void setupNotification(Song song) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromService", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_player);
        remoteViews.setTextViewText(R.id.songName, song.getName());
        remoteViews.setTextViewText(R.id.artistName, song.getArtist().getName());
        if (song.getAlbum().getImage() == 1){
            Bitmap bitmap = getBitmapFromPath(song);
            remoteViews.setImageViewBitmap(R.id.albumCover, bitmap);
        }
        if (mediaPlayer.isPlaying()){
            remoteViews.setOnClickPendingIntent(R.id.pausePlay, buildPendingIntentAction(PAUSE));
            remoteViews.setImageViewResource(R.id.pausePlay, R.drawable.ic_pause_24);
        } else {
            remoteViews.setOnClickPendingIntent(R.id.pausePlay, buildPendingIntentAction(RESUME));
            remoteViews.setImageViewResource(R.id.pausePlay, R.drawable.ic_play_24);
        }
        remoteViews.setOnClickPendingIntent(R.id.nextSong, buildPendingIntentAction(NEXT));
        remoteViews.setOnClickPendingIntent(R.id.previousSong, buildPendingIntentAction(PREVIOUS));

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setDefaults(0)
                .setSmallIcon(R.drawable.icon_notification_cat)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .build();
        startForeground(1, notification);
    }

    private PendingIntent buildPendingIntentAction(int action){
        Intent intent = new Intent(this, ActionReceiver.class);
        intent.putExtra("actionFromService", action);
        return PendingIntent.getBroadcast(this.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    @Override
    public boolean stopService(Intent name) {
        if (mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;
        return super.stopService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onAudioFocusChange(int i) {

    }
}
