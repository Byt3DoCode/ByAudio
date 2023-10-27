package com.byt3.byaudio.model;

import java.io.Serializable;

public class Song implements Serializable {
    int id;
    String name;
    String album;
    String artist;
    int duration;
    String location;

    public Song(int id, String name, String album, String artist, int duration, String location) {
        this.id = id;
        this.name = name;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
