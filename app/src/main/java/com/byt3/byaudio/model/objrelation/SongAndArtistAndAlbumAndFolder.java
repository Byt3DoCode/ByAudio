package com.byt3.byaudio.model.objrelation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.byt3.byaudio.model.Album;
import com.byt3.byaudio.model.Artist;
import com.byt3.byaudio.model.Folder;
import com.byt3.byaudio.model.Song;

import java.io.Serializable;
import java.util.List;

public class SongAndArtistAndAlbumAndFolder implements Serializable {
    @Embedded public Song song;
    @Relation(parentColumn = "songId", entityColumn = "artistId", entity = Artist.class)
    public List<Artist> artist;
    @Relation(parentColumn = "songId", entityColumn = "albumId", entity = Album.class)
    public List<Album> album;
    @Relation(parentColumn = "songId", entityColumn = "folderId", entity = Folder.class)
    public List<Folder> folder;
}
