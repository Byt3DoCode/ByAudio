package com.byt3.byaudio.controller.service;

import static com.byt3.byaudio.utils.functions.CHANNEL_ID;
import static com.byt3.byaudio.utils.functions.MY_LOGCAT;
import static com.byt3.byaudio.utils.functions.buildFullPath;
import static com.byt3.byaudio.utils.functions.cleanName;
import static com.byt3.byaudio.utils.functions.getBitmapFromPath;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.MainActivity;
import com.byt3.byaudio.model.Song;
import com.byt3.byaudio.model.objrelation.CollectionWithSongs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerService extends Service {
    private final MyBinder binder = new MyBinder();
    private boolean started = false;
    private List<CollectionWithSongs> collections;
    private ArrayList<Song> list;
    private int index, repeatMode;
    public MutableLiveData<Song> songLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPlayingLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> indexLiveData = new MutableLiveData<>();
    public Observer<List<CollectionWithSongs>> collectionsObserver = new Observer<List<CollectionWithSongs>>() {
        @Override
        public void onChanged(List<CollectionWithSongs> collectionWithSongs) {
            collections = collectionWithSongs;
        }
    };
    private MediaPlayer mediaPlayer = null;
    public static final int PLAY = 0;
    public static final int PAUSE = 2;
    public static final int RESUME = 3;
    public static final int NEXT = 4;
    public static final int PREVIOUS = 5;
    public static final int FORWARD = 6;
    public static final int REWIND = 7;
    public static final int REPEAT_OFF = 8;
    public static final int REPEAT_ONE = 9;
    public static final int REPEAT_ALL = 10;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mediaPlayer -> playNextSong());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        started = true;
        if (list == null || intent.getBooleanExtra("newList", false)) {
            list = intent.getParcelableArrayListExtra("songs");
            index = intent.getIntExtra("index", 0);
            for (Song s : list) {
                Log.d(MY_LOGCAT, s.getAlbum().toString());
            }
        }
        int action = intent.getIntExtra("actionToService", 0);
        if (list != null)
            handleAction(action);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        started = false;
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        started = false;
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void handleAction(int action) {
        switch (action) {
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
        repeatMode = REPEAT_OFF;
        try {
            mediaPlayer.reset();
            mediaPlayer.setOnCompletionListener(mediaPlayer -> playNextSong());
            mediaPlayer.setDataSource(buildFullPath(list.get(index)));
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mediaPlayer.start();
        songLiveData.setValue(list.get(index));
        isPlayingLiveData.setValue(true);
        setupNotification(list.get(index));
    }
    public void pauseAudio() {
        mediaPlayer.pause();
        isPlayingLiveData.setValue(false);
        setupNotification(list.get(index));
    }
    public void resumeAudio() {
        mediaPlayer.start();
        isPlayingLiveData.setValue(true);
        setupNotification(list.get(index));
    }
    public void playNextSong() {
        if (repeatMode == REPEAT_ONE) {
            playAudio();
            return;
        }
        if (index < list.size() - 1){
            ++index;
            playAudio();
        } else if (repeatMode == REPEAT_ALL){
            index = 0;
            playAudio();
        } else if (repeatMode == REPEAT_OFF) {
            isPlayingLiveData.setValue(false);
        }
        indexLiveData.setValue(index);
    }
    public void playPreviousSong() {
        if (repeatMode == REPEAT_ONE) {
            playAudio();
            return;
        }
        if (index > 0){
            --index;
            playAudio();
        } else if (repeatMode == REPEAT_ALL){
            index = list.size() - 1;
            playAudio();
        } else if (repeatMode == REPEAT_OFF) {
            isPlayingLiveData.setValue(false);
        }
        indexLiveData.setValue(index);
    }
    public void fastForward() {
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000, MediaPlayer.SEEK_NEXT_SYNC);
    }
    public void rewind() {
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000, MediaPlayer.SEEK_PREVIOUS_SYNC);
    }
    public void setRepeatMode(int mode) {
        repeatMode = mode;
    }

    private void setupNotification(Song song) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromService", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap;
        if (song.getAlbum().getImage() == 1 || song.getAlbum().getImage() == 2)
            bitmap = getBitmapFromPath(song);
        else
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_cat);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setDefaults(0)
                .setShowWhen(false)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.icon_notification_cat)
                .setContentTitle(cleanName(song.getName()))
                .setContentText(song.getArtist().getName())
                .setLargeIcon(bitmap)
                .addAction(R.drawable.ic_previous_24, "previous", buildPendingIntentAction(PREVIOUS))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                );
        if (mediaPlayer.isPlaying())
            notifyBuilder.addAction(R.drawable.ic_pause_24, "pause", buildPendingIntentAction(PAUSE));
        else
            notifyBuilder.addAction(R.drawable.ic_play_24, "pause", buildPendingIntentAction(RESUME));
        notifyBuilder.addAction(R.drawable.ic_next_24, "next", buildPendingIntentAction(NEXT));
        startForeground(1, notifyBuilder.build());
    }

    private PendingIntent buildPendingIntentAction(int action){
        Intent intent = new Intent(this, ActionReceiver.class);
        intent.putExtra("actionFromService", action);
        return PendingIntent.getBroadcast(this.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public Song getSong() {
        return list.get(index);
    }
    public List<Song> getList() {
        return list;
    }
    public int getIndex() {
        return index;
    }
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    public void setList(ArrayList<Song> songs) {
        this.list = songs;
    }
    public boolean hasStarted(){
        return started;
    }
    public int getRepeatMode() {
        return repeatMode;
    }

    public class MyBinder extends Binder {
        public PlayerService getplayerService(){
            return PlayerService.this;
        }
    }
}
