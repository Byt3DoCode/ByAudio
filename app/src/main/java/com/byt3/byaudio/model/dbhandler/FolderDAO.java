package com.byt3.byaudio.model.dbhandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.byt3.byaudio.model.Folder;

import java.util.List;

@Dao
public interface FolderDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Folder... folders);
    @Update
    void updateFolder(Folder folder);
    @Delete
    void delete(Folder folder);
    @Query("SELECT * FROM Folder")
    List<Folder> getAll();

}
