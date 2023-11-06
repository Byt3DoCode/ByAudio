package com.byt3.byaudio.model.dbhandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.byt3.byaudio.model.Song;

import java.util.List;

@Dao
public interface SongDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Song... songs);
    @Update
    void updateFolder(Song song);
    @Delete
    void delete(Song song);
    @Query("SELECT * FROM Song")
    List<Song> getAll();
}
