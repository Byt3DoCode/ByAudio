package com.byt3.byaudio.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Artist implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    int artistId;
    @ColumnInfo(name = "artist_name")
    String name;

    public Artist(){}

    @Ignore
    public Artist(String name) {
        this.name = name;
    }

    @Ignore
    protected Artist(Parcel in) {
        artistId = in.readInt();
        name = in.readString();
    }

    @Ignore
    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(artistId);
        parcel.writeString(name);
    }
}
