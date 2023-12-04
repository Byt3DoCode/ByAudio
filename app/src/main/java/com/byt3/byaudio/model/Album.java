package com.byt3.byaudio.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Album implements Parcelable, Serializable {
    @PrimaryKey(autoGenerate = true)
    int albumId;
    @ColumnInfo(name = "album_name")
    String name;
    @ColumnInfo(name = "album_cover")
    int image;
    @Ignore
    String url;

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

    @Ignore
    public Album(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Ignore
    public Album(int image, String url) {
        this.image = image;
        this.url = url;
    }

    @Ignore
    protected Album(Parcel in) {
        albumId = in.readInt();
        name = in.readString();
        image = in.readInt();
        url = in.readString();
    }

    @Ignore
    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(albumId);
        parcel.writeString(name);
        parcel.writeInt(image);
        parcel.writeString(url);
    }
}
