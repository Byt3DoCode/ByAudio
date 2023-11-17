package com.byt3.byaudio.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity
public class SongCollection implements Serializable {
    @Ignore
    public static final String TYPE_QUEUE = "queue";
    @Ignore
    public static final String TYPE_PLAYLIST = "playlist";
    @PrimaryKey(autoGenerate = true)
    public int scId;
    public String scName;
    public String scType;
}
