package com.byt3.byaudio.controller.service;

import static com.byt3.byaudio.utils.functions.CHANNEL_ID;
import static com.byt3.byaudio.utils.functions.buildFullPath;
import static com.byt3.byaudio.utils.functions.cleanName;
import static com.byt3.byaudio.utils.functions.getBitmapFromPath;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media3.exoplayer.ExoPlayer;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.MainActivity;
import com.byt3.byaudio.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {
    private final MyBinder binder = new MyBinder();
    private boolean started = false;
    private ArrayList<Song> list;
    private int index;
    private boolean loopQueue = false;
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
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        started = true;
        if (list == null || intent.getBooleanExtra("newList", false)) {
            list = intent.getParcelableArrayListExtra("songs");
            index = intent.getIntExtra("index", 0);
        }
        int action = intent.getIntExtra("actionToService", 0);
        if (list != null)
            handleAction(action);
        //ToDo:To actively send data from a service to an activity or have an activity listen to
        // changes in data, you can use various mechanisms such as callbacks, broadcast receivers,
        // or event bus libraries. Here's an example using callbacks:
        //
        //1. Define an interface in your service class that represents the callback methods to be
        // implemented by the activity. For example:
        //
        //java
        //public interface MediaPlayerCallback {
        //    void onSongComplete(String nextSong);
        //}
        //
        //
        //2. Implement this interface in your activity class and override the callback method. For
        // example:
        //
        //java
        //public class MainActivity extends AppCompatActivity implements MediaPlayerCallback {
        //    // ...
        //
        //    @Override
        //    public void onSongComplete(String nextSong) {
        //        // Update the UI with the next song information
        //        runOnUiThread(() -> {
        //            // Display the next song in your activity
        //        });
        //    }
        //}
        //
        //
        //3. In your service class, create a reference to the callback interface and invoke the
        // callback method when needed. For example:
        //
        //java
        //public class MyService extends Service {
        //    private MediaPlayerCallback callback;
        //
        //    // ...
        //
        //    public void setMediaPlayerCallback(MediaPlayerCallback callback) {
        //        this.callback = callback;
        //    }
        //
        //    private void onSongComplete(String nextSong) {
        //        if (callback != null) {
        //            callback.onSongComplete(nextSong);
        //        }
        //    }
        //}
        //
        //
        //4. When starting the service from your activity, make sure to set the callback using the
        // `setMediaPlayerCallback()` method. For example:
        //
        //java
        //MyService myService = new MyService();
        //myService.setMediaPlayerCallback(this); // 'this' refers to the activity instance
        //startService(intent);
        //
        //
        //By implementing this approach, the service can actively send data to the activity by
        // invoking the callback method defined in the interface. The activity will then receive the
        // data and update its UI accordingly.
        return START_NOT_STICKY;
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

    public void pauseAudio() {
        mediaPlayer.pause();
        setupNotification(list.get(index));
    }

    public void resumeAudio() {
        mediaPlayer.start();
        setupNotification(list.get(index));
    }

    public void playNextSong() {
        if (index < list.size() - 1)
            ++index;
        else if (loopQueue)
            index = 0;
        playAudio();
    }

    public void playPreviousSong() {
        if (index > 0)
            --index;
        else if (loopQueue)
            index = list.size() - 1;
        playAudio();
    }

    public void fastForward() {
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000, MediaPlayer.SEEK_NEXT_SYNC);
    }

    public void rewind() {
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000, MediaPlayer.SEEK_PREVIOUS_SYNC);
    }

    private void setLoopSong() {

    }

    private void setLoopQueue() {

    }

    private void setupNotification(Song song) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromService", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap;
        if (song.getAlbum().getImage() == 1)
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
            notifyBuilder.addAction(R.drawable.ic_play_24, "pause", buildPendingIntentAction(PAUSE));
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
    public String test(){return "hello";}

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

    public void setList(ArrayList<Song> songs) {
        this.list = songs;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean hasStarted(){
        return started;
    }

    public class MyBinder extends Binder {
        public PlayerService getplayerService(){
            return PlayerService.this;
        }
    }
}
