package com.byt3.byaudio.model.dbhandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.byt3.byaudio.model.Album;
import com.byt3.byaudio.model.Artist;
import com.byt3.byaudio.model.Song;

import java.util.List;

@Dao
public interface AlbumDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Album album);
    @Update
    void updateFolder(Album album);
    @Delete
    void delete(Album album);
    @Query("SELECT * FROM Album")
    List<Album> getAll();
    @Query("SELECT * FROM Album WHERE album_name = :name")
    Album getAlbumByName(String name);
    @Query("SELECT * FROM Album WHERE albumId = :albumId")
    Album getAlbumById(int albumId);
    static void bindAlbumToSong(Song song, Album album, AlbumDAO albumDAO){
        Album queryResult = albumDAO.getAlbumByName(album.getName());
        if (queryResult != null){
            song.setsAlbumId(queryResult.getAlbumId());
        } else {
            album.setAlbumId(Math.toIntExact(albumDAO.insert(album)));
            song.setsAlbumId(album.getAlbumId());
        }
    }
}
