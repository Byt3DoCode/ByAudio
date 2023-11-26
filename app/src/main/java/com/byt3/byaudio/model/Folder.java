package com.byt3.byaudio.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Folder implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int folderId;
    @ColumnInfo(name = "folder_name")
    private String name;
    @ColumnInfo(name = "folder_path")
    private String path;
    @ColumnInfo(name = "folder_size")
    private int size;

    public Folder() {}

    @Ignore
    public Folder(String path) {
        this.path = path;
    }

    @Ignore
    public Folder(String name, String path, int size) {
        this.name = name;
        this.path = path;
        this.size = size;
    }

    @Ignore
    protected Folder(Parcel in) {
        folderId = in.readInt();
        name = in.readString();
        path = in.readString();
        size = in.readInt();
    }

    @Ignore
    public static final Creator<Folder> CREATOR = new Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel in) {
            return new Folder(in);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };

    public int getFolderId() {
        return folderId;
    }
    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(folderId);
        parcel.writeString(name);
        parcel.writeString(path);
        parcel.writeInt(size);
    }
}
