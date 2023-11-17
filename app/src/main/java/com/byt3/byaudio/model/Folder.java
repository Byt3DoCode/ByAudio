package com.byt3.byaudio.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Folder implements Serializable {
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
}
