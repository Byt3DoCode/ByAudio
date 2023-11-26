package com.byt3.byaudio.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(indices = {@Index(value = {"scName", "scSize"}, unique = true)})
public class SongCollection implements Parcelable {
    @Ignore
    public static final String TYPE_QUEUE = "queue";
    @Ignore
    public static final String TYPE_PLAYLIST = "playlist";
    @PrimaryKey(autoGenerate = true)
    public int scId;
    private String scName;
    private String scType;
    private int scSize;
    private long scTotalDuration;

    public SongCollection() {
    }

    @Ignore
    public SongCollection(String scName, String scType, int scSize, long scTotalDuration) {
        this.scName = scName;
        this.scType = scType;
        this.scSize = scSize;
        this.scTotalDuration = scTotalDuration;
    }

    @Ignore
    protected SongCollection(Parcel in) {
        scId = in.readInt();
        scName = in.readString();
        scType = in.readString();
        scSize = in.readInt();
        scTotalDuration = in.readLong();
    }

    @Ignore
    public static final Creator<SongCollection> CREATOR = new Creator<SongCollection>() {
        @Override
        public SongCollection createFromParcel(Parcel in) {
            return new SongCollection(in);
        }

        @Override
        public SongCollection[] newArray(int size) {
            return new SongCollection[size];
        }
    };

    public int getScId() {
        return scId;
    }

    public void setScId(int scId) {
        this.scId = scId;
    }

    public String getScName() {
        return scName;
    }

    public void setScName(String scName) {
        this.scName = scName;
    }

    public String getScType() {
        return scType;
    }

    public void setScType(String scType) {
        this.scType = scType;
    }

    public int getScSize() {
        return scSize;
    }

    public void setScSize(int scSize) {
        this.scSize = scSize;
    }

    public long getScTotalDuration() {
        return scTotalDuration;
    }

    public void setScTotalDuration(long scTotalDuration) {
        this.scTotalDuration = scTotalDuration;
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(scId);
        parcel.writeString(scName);
        parcel.writeString(scType);
        parcel.writeInt(scSize);
        parcel.writeLong(scTotalDuration);
    }
}
