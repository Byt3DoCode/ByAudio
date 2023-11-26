package com.byt3.byaudio.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import com.byt3.byaudio.model.Song;

import java.io.IOException;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class functions {
    public static final String MY_LOGCAT = "MyLog";
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
        String finalTimerString = "";
        String secondsString;

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
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
        name = name.substring(0, name.length() - splitByDot[splitByDot.length - 1].length() - 1);
        return name;
    }

    public static String buildFullPath(Song song){
        return song.getFolder().getPath()+"/"+song.getName();
    }
}
//        ActionReceiver myreceiver = new ActionReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(myreceiver, intentFilter);
