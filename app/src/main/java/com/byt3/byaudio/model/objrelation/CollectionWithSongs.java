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
            associateBy = @Junction(CollectionSongCrossRef.class)
    )
    public List<Song> songs;
}
