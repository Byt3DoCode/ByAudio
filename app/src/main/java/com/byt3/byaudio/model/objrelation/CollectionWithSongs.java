package com.byt3.byaudio.model.objrelation;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.byt3.byaudio.model.CollectionSongCrossRef;
import com.byt3.byaudio.model.Song;
import com.byt3.byaudio.model.SongCollection;

import java.util.List;

public class CollectionWithSongs {
    @Embedded public SongCollection songCollection;
    @Relation(
            parentColumn = "scId",
            entityColumn = "songId",
            associateBy = @Junction(
                    value = CollectionSongCrossRef.class,
                    parentColumn = "crSCId",
                    entityColumn = "crSongId")
    )
    public List<Song> songs;

    public SongCollection getSongCollection() {
        return songCollection;
    }

    public void setSongCollection(SongCollection songCollection) {
        this.songCollection = songCollection;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
