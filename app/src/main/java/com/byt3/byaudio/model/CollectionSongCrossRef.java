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
                @Index(value = {"crSCId", "crSongOrder"}, unique = true),
                @Index("crSongId")}
)
public class CollectionSongCrossRef {
    public int crSCId;
    public int crSongId;
    private int crSongOrder;
    public CollectionSongCrossRef() {
    }
    @Ignore
    public CollectionSongCrossRef(int crSCId, int crSongId, int crSongOrder) {
        this.crSCId = crSCId;
        this.crSongId = crSongId;
        this.crSongOrder = crSongOrder;
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

    public int getCrSongOrder() {
        return crSongOrder;
    }

    public void setCrSongOrder(int crSongOrder) {
        this.crSongOrder = crSongOrder;
    }
}
