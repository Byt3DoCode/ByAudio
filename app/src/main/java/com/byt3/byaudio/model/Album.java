package com.byt3.byaudio.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Album implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int albumId;
    @ColumnInfo(name = "album_name")
    String name;
    @ColumnInfo(name = "album_cover")
    int image;

    public Album(){}

    @Ignore
    public Album(String name){
        this.name = name;
    }

    @Ignore
    public Album(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
