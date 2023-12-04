package com.byt3.byaudio.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.byt3.byaudio.model.Song;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class functions {
    public static final String MY_LOGCAT = "MyLog";
    public static final String FIREBASE_STORAGE = "firebase";
    public static final String CHANNEL_ID = "1001";
    private static final String[] fileExtensions = {
            "mp3",
            "wav",
            "flac",
            "aac",
            "ogg",
            "wma",
            "aiff",
            "m4a",
            "amr",
            "midi"
    };
    private static final HashSet<String> fileExtensionsHash = Stream.of(fileExtensions).collect(Collectors.toCollection(HashSet::new));

    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "", secondsString;

        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0)
            finalTimerString = hours + ":";

        if (seconds < 10)
            secondsString = "0" + seconds;
        else
            secondsString = "" + seconds;

        finalTimerString = finalTimerString + minutes + ":" + secondsString;
        return finalTimerString;
    }

    public static Boolean possibleAudioFolderName(String fileName) {
        String lowerCased = fileName.toLowerCase();
        if (fileName.startsWith("com.") || fileName.startsWith("."))
            return false;
        return lowerCased.contains("song") ||
                lowerCased.contains("sound") ||
                lowerCased.contains("music") ||
                lowerCased.contains("download") ||
                lowerCased.contains("audio") ||
                lowerCased.contains("mp3");
    }

    public static Boolean checkMicroSD(String folderName) {
        return !folderName.equals("self") && !folderName.equals("emulated");
    }

    public static Boolean isAudio(String fileName) {
        String[] splitName = fileName.split("\\.");
        String fileExtension = splitName[splitName.length - 1];
        return fileExtensionsHash.contains(fileExtension.toLowerCase());
    }

    public static Bitmap getBitmapFromPath(Song song) {
        if (song.getFolder().getName().equals(FIREBASE_STORAGE)) {
            final Bitmap[] bm = new Bitmap[1];
            Picasso.get().load(song.getAlbum().getUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    bm[0] = bitmap;
                }
                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {}
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {}
            });
            return bm[0];
        }
        else
            try {
                String path = buildFullPath(song);
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(path);
                byte[] artBytes = mmr.getEmbeddedPicture();
                assert artBytes != null;
                Bitmap bm = BitmapFactory.decodeByteArray(artBytes,0,artBytes.length);
                mmr.close();
                return bm;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    public static String cleanName(String name) {
        name = name.replace("y2mate.com - ", "")
                .replaceAll("_", " ");
        String[] splitByDot = name.split("\\.");
        if (splitByDot.length != 1)
            name = name.substring(0, name.length() - splitByDot[splitByDot.length - 1].length() - 1);
        return name;
    }

    public static String buildFullPath(Song song){
        return song.getFolder().getPath()+"/"+song.getName();
    }
}
