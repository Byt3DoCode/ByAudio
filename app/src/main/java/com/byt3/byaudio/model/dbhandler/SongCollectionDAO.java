package com.byt3.byaudio.model.dbhandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.byt3.byaudio.model.CollectionSongCrossRef;
import com.byt3.byaudio.model.Song;
import com.byt3.byaudio.model.SongCollection;

import java.util.List;

@Dao
public interface SongCollectionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(SongCollection songCollection);
    @Update
    void updateCollection(SongCollection songCollection);
    @Delete
    void delete(SongCollection songCollection);
    @Query("SELECT * FROM SongCollection WHERE scName = :name")
    SongCollection getCollectionByName(String name);
    @Query("SELECT * FROM SongCollection WHERE scType = :type")
    List<SongCollection> getCollectionByType(String type);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCollectionSongCrossRef(CollectionSongCrossRef collectionSongCrossRef);
}
