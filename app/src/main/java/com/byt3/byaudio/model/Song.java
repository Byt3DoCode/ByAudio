package com.byt3.byaudio.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Artist.class, parentColumns = "artistId", childColumns = "sArtistId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Album.class, parentColumns = "albumId", childColumns = "sAlbumId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Folder.class, parentColumns = "folderId", childColumns = "sFolderId", onDelete = ForeignKey.CASCADE)},
        indices = {
                @Index("sArtistId"),
                @Index("sAlbumId"),
                @Index(value = {"sFolderId", "song_name"}, unique = true)
        })
public class Song implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    int songId;
    @ColumnInfo(name = "song_name")
    String name;
    @ColumnInfo(name = "song_duration")
    int duration;
    public int sAlbumId;
    public int sArtistId;
    public int sFolderId;
    @Ignore
    Folder folder;
    @Ignore
    Album album;
    @Ignore
    Artist artist;

    public Song() {
    }
    @Ignore
    public Song(String name, Folder folder){
        this.name = name;
        this.folder = folder;
    }

    @Ignore
    protected Song(Parcel in) {
        songId = in.readInt();
        name = in.readString();
        duration = in.readInt();
        album = in.readTypedObject(Album.CREATOR);
        artist = in.readTypedObject(Artist.CREATOR);
        folder = in.readTypedObject(Folder.CREATOR);
    }

    @Ignore
    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
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

    public int getsAlbumId() {
        return sAlbumId;
    }

    public void setsAlbumId(int sAlbumId) {
        this.sAlbumId = sAlbumId;
    }

    public int getsArtistId() {
        return sArtistId;
    }

    public void setsArtistId(int sArtistId) {
        this.sArtistId = sArtistId;
    }

    public int getsFolderId() {
        return sFolderId;
    }

    public void setsFolderId(int sFolderId) {
        this.sFolderId = sFolderId;
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

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(songId);
        parcel.writeString(name);
        parcel.writeInt(duration);
        parcel.writeTypedObject(album, i);
        parcel.writeTypedObject(artist, i);
        parcel.writeTypedObject(folder, i);
    }
}
