package com.byt3.byaudio.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import java.io.IOException;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class functions {
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

    public static Bitmap getBitmapFromPath(String path) {
        try {
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
//        StringBuilder cleanName = new StringBuilder();
//        char[] deleteParentheses = name.toCharArray();
//        boolean delete = false;
//        for (int i = 0; i < name.length(); ++i){
//            if (deleteParentheses[i] == '(' || deleteParentheses[i] == '[' || deleteParentheses[i] == '{')
//                delete = true;
//            if (deleteParentheses[i] == ')' || deleteParentheses[i] == ']' || deleteParentheses[i] == '}')
//                delete = false;
//            if (!delete)
//                cleanName.append(deleteParentheses[i]);
//        }
//        String result = cleanName.toString();
//
//        cleanName = new StringBuilder();
//        String[] splitByDot = name.split("\\.");
//        int urlAt = -1;
//        for (int i = 0; i < splitByDot.length-1; ++i)
//            if (splitByDot[i].startsWith("com"))
//                urlAt = i-1;
//        if (urlAt != -1){
//            char[] checkUrl = splitByDot[urlAt].toCharArray();
//            for (int i = checkUrl.length - 1; i >= 0; --i) {
//                if (Character.isAlphabetic(checkUrl[i]) || Character.isDigit(checkUrl[i]))
//                    checkUrl[i] = '?';
//                else
//                    break;
//            }
//            String s = checkUrl.toString().replaceAll("\\?", "");
//        }
//
//
//        cleanName = new StringBuilder(name.substring(0, name.length() - splitByDot[splitByDot.length - 1].length() - 1));
//        String replaceUnderline = cleanName.toString().replaceAll("_", " ");
//        String[] splitBySpace = cleanName.toString().split(" ");
//        return cleanName.toString();
        return name.replace(".mp3", "")
                .replace("y2mate.com - ", "")
                .replaceAll("_", " ");
    }
}
