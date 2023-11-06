package com.byt3.byaudio.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.byt3.byaudio.model.dbhandler.AlbumDAO;
import com.byt3.byaudio.model.dbhandler.ArtistDAO;
import com.byt3.byaudio.model.dbhandler.FolderDAO;
import com.byt3.byaudio.model.dbhandler.SongDAO;

@Database(entities = {Folder.class, Song.class, Artist.class, Album.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FolderDAO folderDAO();
    public abstract SongDAO songDAO();
    public abstract ArtistDAO artistDAO();
    public abstract AlbumDAO albumDAO();
}
