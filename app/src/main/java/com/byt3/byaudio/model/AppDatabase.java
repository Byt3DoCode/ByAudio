package com.byt3.byaudio.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.byt3.byaudio.model.dbhandler.AlbumDAO;
import com.byt3.byaudio.model.dbhandler.ArtistDAO;
import com.byt3.byaudio.model.dbhandler.CoSosDAO;
import com.byt3.byaudio.model.dbhandler.FolderDAO;
import com.byt3.byaudio.model.dbhandler.SoArAlFoDAO;
import com.byt3.byaudio.model.dbhandler.SongCollectionDAO;
import com.byt3.byaudio.model.dbhandler.SongDAO;
import com.byt3.byaudio.model.objrelation.SongAndArtistAndAlbumAndFolder;

@Database(entities = {
        Folder.class,
        Song.class,
        Artist.class,
        Album.class,
//        SongCollection.class,
//        CollectionSongCrossRef.class
        }, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "database.db";
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    protected AppDatabase() {};

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME)
                .allowMainThreadQueries()
                .build();
    }
    public abstract FolderDAO folderDAO();
    public abstract SongDAO songDAO();
    public abstract ArtistDAO artistDAO();
    public abstract AlbumDAO albumDAO();
    public abstract SoArAlFoDAO saafDAO();
//    public abstract SongCollectionDAO songCollectionDAO();
//    public abstract CoSosDAO CoSosDAO();
}
