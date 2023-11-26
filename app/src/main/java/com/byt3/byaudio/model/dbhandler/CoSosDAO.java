package com.byt3.byaudio.model.dbhandler;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.byt3.byaudio.model.objrelation.CollectionWithSongs;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface CoSosDAO {
    @Transaction
    @Update
    void update(CollectionWithSongs collectionWithSongs);
    @Transaction
    @Query("SELECT * FROM SongCollection")
    List<CollectionWithSongs> getCollectionWithSongs();

    @Transaction
    @Query("SELECT * FROM SongCollection WHERE scId = :id")
    CollectionWithSongs getCollectionWithSongsByscId(int id);

    @Transaction
    @Query("SELECT * FROM SongCollection WHERE scType = :type")
    Flowable<List<CollectionWithSongs>> getCollectionWithSongsByType(String type);
}
