package com.byt3.byaudio.model.dbhandler;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.byt3.byaudio.model.objrelation.SongAndArtistAndAlbumAndFolder;

import java.util.List;

@Dao
public interface SoArAlFoDAO {
    @Transaction
    @Query("SELECT * FROM Song WHERE song_name LIKE :name")
    List<SongAndArtistAndAlbumAndFolder> getSAAFBySongName(String name);
}
