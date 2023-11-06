package com.byt3.byaudio.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(foreignKeys = {@ForeignKey(entity = Artist.class, parentColumns = "id", childColumns = "artistId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Album.class, parentColumns = "id", childColumns = "albumId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Folder.class, parentColumns = "id", childColumns = "folderId", onDelete = ForeignKey.CASCADE)})
public class Song implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "song_name")
    String name;
    @ColumnInfo(name = "song_duration")
    int duration;
    int albumId;
    int artistId;
    int folderId;
    @Ignore
    Folder folder;
    @Ignore
    Album album;
    @Ignore
    Artist artist;


    public Song() {
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
