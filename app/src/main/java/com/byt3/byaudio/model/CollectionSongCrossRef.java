package com.byt3.byaudio.model;

import androidx.room.Entity;

@Entity(primaryKeys = {"scId", "songId"})
public class CollectionSongCrossRef {
    public int scId;
    public int songId;
}
