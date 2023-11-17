package com.byt3.byaudio.model.dbhandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.byt3.byaudio.model.Album;
import com.byt3.byaudio.model.Artist;
import com.byt3.byaudio.model.Folder;
import com.byt3.byaudio.model.Song;

import java.util.List;

@Dao
public interface ArtistDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Artist artist);
    @Update
    void updateFolder(Artist artist);
    @Delete
    void delete(Artist artist);
    @Query("SELECT * FROM Artist")
    List<Artist> getAll();
    @Query("SELECT * FROM Artist WHERE artist_name = :name")
    Artist getArtistByName(String name);
    @Query("SELECT * FROM Artist WHERE artistId = :artistId")
    Artist getArtistById(int artistId);
    static void bindArtistToSong(Song song, Artist artist, ArtistDAO artistDAO){
        Artist queryResult = artistDAO.getArtistByName(artist.getName());
        if (queryResult != null){
            song.setsArtistId(queryResult.getArtistId());
        } else {
            artist.setArtistId(Math.toIntExact(artistDAO.insert(artist)));
            song.setsArtistId(artist.getArtistId());
        }
    }
}
