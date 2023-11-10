package com.byt3.byaudio.utils;

public class functions {
    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

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

    public static Boolean checkFolderName(String fileName){
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
}
