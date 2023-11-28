package com.byt3.byaudio.model.dbhandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.room.Upsert;

import com.byt3.byaudio.model.CollectionSongCrossRef;
import com.byt3.byaudio.model.objrelation.CollectionWithSongs;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface CoSosDAO {
    @Transaction
    @Query("SELECT * FROM SongCollection")
    List<CollectionWithSongs> getCollectionWithSongs();

    @Transaction
    @Query("SELECT * FROM SongCollection WHERE scId = :id")
    CollectionWithSongs getCollectionWithSongsByscId(int id);

    @Transaction
    @Query("SELECT * FROM SongCollection WHERE scType = :type")
    Flowable<List<CollectionWithSongs>> getCollectionWithSongsByType(String type);

    @Transaction
    @Delete
    void deleteCrossRef (CollectionSongCrossRef collectionSongCrossRef);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCollectionSongCrossRef(CollectionSongCrossRef collectionSongCrossRef);

    @Transaction
    @Update
    void updateCollectionSongCrossRef(CollectionSongCrossRef collectionSongCrossRef);
}
