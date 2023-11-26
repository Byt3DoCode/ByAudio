package com.byt3.byaudio.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(
        primaryKeys = {"crSCId", "crSongId"},
        foreignKeys = {@ForeignKey(
                entity = SongCollection.class,
                parentColumns = "scId",
                childColumns = "crSCId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)},
        indices = {
                @Index("crSCId"),
                @Index("crSongId")}
)
public class CollectionSongCrossRef {
    public int crSCId;
    public int crSongId;

    public CollectionSongCrossRef() {
    }
    @Ignore
    public CollectionSongCrossRef(int crSCId, int crSongId) {
        this.crSCId = crSCId;
        this.crSongId = crSongId;
    }

    public int getCrSCId() {
        return crSCId;
    }

    public void setCrSCId(int crSCId) {
        this.crSCId = crSCId;
    }

    public int getCrSongId() {
        return crSongId;
    }

    public void setCrSongId(int crSongId) {
        this.crSongId = crSongId;
    }
}
