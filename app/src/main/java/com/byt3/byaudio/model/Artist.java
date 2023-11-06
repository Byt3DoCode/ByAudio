package com.byt3.byaudio.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Artist implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "artist_name")
    String name;

    public Artist(String name) {
        this.name = name;
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
}
