package com.byt3.byaudio.model.dbhandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.byt3.byaudio.model.Artist;

import java.util.List;

@Dao
public interface ArtistDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Artist... artists);
    @Update
    void updateFolder(Artist artist);
    @Delete
    void delete(Artist artist);
    @Query("SELECT * FROM Artist")
    List<Artist> getAll();
}
