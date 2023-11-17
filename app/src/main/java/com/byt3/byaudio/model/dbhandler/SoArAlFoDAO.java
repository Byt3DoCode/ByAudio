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

    @Query("SELECT * FROM Song JOIN Album on albumId = sAlbumId JOIN Artist on artistId = sArtistId JOIN Folder on folderId = sFolderId WHERE song_name like :name")
    List<SongAndArtistAndAlbumAndFolder> getSAAFBySongName2(String name);
}
