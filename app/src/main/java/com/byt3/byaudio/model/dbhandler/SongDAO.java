package com.byt3.byaudio.model.dbhandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.byt3.byaudio.model.Folder;
import com.byt3.byaudio.model.Song;

import java.util.List;
import java.util.Set;

@Dao
public interface SongDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Song song);
    @Update
    void updateFolder(Song song);
    @Delete
    void delete(Song song);
    @Query("SELECT * FROM Song")
    List<Song> getAll();
    @Query("SELECT * FROM Song WHERE sFolderId = :folderId")
    List<Song> getSongByFolder(int folderId);
    @Query("SELECT song_name FROM Song WHERE sFolderId = :folderId")
    List<String> getSongNameByFolder(int folderId);
    @Query("SELECT * FROM Song WHERE song_name like :query")
    List<Song> getSongLikeName(String query);
}
