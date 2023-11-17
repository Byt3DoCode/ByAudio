package com.byt3.byaudio.model.dbhandler;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.byt3.byaudio.model.objrelation.CollectionWithSongs;

import java.util.List;

@Dao
public interface CoSosDAO {
    @Transaction
    @Query("SELECT * FROM SongCollection")
    public List<CollectionWithSongs> getCollectionWithSongs();

}
