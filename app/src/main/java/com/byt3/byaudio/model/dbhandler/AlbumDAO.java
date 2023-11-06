package com.byt3.byaudio.model.dbhandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.byt3.byaudio.model.Album;

import java.util.List;

@Dao
public interface AlbumDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Album... albums);
    @Update
    void updateFolder(Album album);
    @Delete
    void delete(Album album);
    @Query("SELECT * FROM Album")
    List<Album> getAll();
}
