package com.byt3.byaudio.controller;

import static com.byt3.byaudio.utils.functions.checkMicroSD;
import static com.byt3.byaudio.utils.functions.isAudio;
import static com.byt3.byaudio.utils.functions.possibleAudioFolderName;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.byt3.byaudio.R;
import com.byt3.byaudio.model.Album;
import com.byt3.byaudio.model.AppDatabase;
import com.byt3.byaudio.model.Artist;
import com.byt3.byaudio.model.Folder;
import com.byt3.byaudio.model.Song;
import com.byt3.byaudio.model.SongCollection;
import com.byt3.byaudio.model.dbhandler.AlbumDAO;
import com.byt3.byaudio.model.dbhandler.ArtistDAO;
import com.byt3.byaudio.model.dbhandler.FolderDAO;
import com.byt3.byaudio.model.dbhandler.SongDAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    final int REQUEST_PERMISSION_READING_STATE = 1;
    private static final String RequiredPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    List<Song> songList = new ArrayList<>();
    List<Folder> folderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        HandlePermission();
    }

    private void nextActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }

    private void updateDB() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, RequiredPermission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            FolderDAO folderDAO = db.folderDAO();
            SongDAO songDAO = db.songDAO();
            List<Folder> folders = folderDAO.getAll();
            for (Folder folder : folders) {
                File[] filesInFolder = new File(folder.getPath()).listFiles();
                if (filesInFolder != null)
                    if (folder.getSize() < filesInFolder.length) {
                        List<String> songNames = songDAO.getSongNameByFolder(folder.getFolderId());
                        for (File f : filesInFolder)
                            if (!songNames.contains(f.getName())){
                                songDAO.insert(new Song(f.getName(),folder));
                                //TODO: add code insert album & artist of song
                            }
                    } else if (folder.getSize() > filesInFolder.length) {
                        List<Song> songs = songDAO.getSongByFolder(folder.getFolderId());
                        for (int i = 0; i < songs.size(); ++i){
                            if (!songs.get(i).getName().equals(filesInFolder[i].getName())){
                                //TODO: add code select exist album & artist to delete
                                songDAO.delete(songs.remove(i));
                                --i;
                            }
                        }
                    }
                else {
                    Log.e("MyLog", "Folder " + folder.getName() + " is empty, deleting...");
                    folderDAO.delete(folder);
                }
            }
        }
    }

    private void firstRuntimeDataPopulate() {
        List<File> files = new ArrayList<>();
        files.add(Environment.getExternalStorageDirectory());
        File storage = new File("/storage");
        for (File f : Objects.requireNonNull(storage.listFiles()))
            if (checkMicroSD(f.getName()))
                files.add(f);

        for (File file : files)
            for (File f : Objects.requireNonNull(file.listFiles()))
                if (possibleAudioFolderName(f.getName()))
                    searchValidFolder(f, f.getName());

        populateData();
    }

    private void searchValidFolder(File file, String directory) {
        File[] list = file.listFiles();
        boolean folderFound = false;
        File mFile;
        String directoryName;
        int counter = 0;
        if (list != null) {
            for (File value : list) {
                mFile = new File(file, value.getName());
                if (mFile.isDirectory()) {
                    directoryName = value.getName();
                    searchValidFolder(mFile, directoryName);
                } else {
                    if (isAudio(value.getName())) {
                        ++counter;
                        songList.add(new Song(value.getName(), new Folder(value.getParent())));
                        folderFound = true;
                    }
                }
            }
        }
        if (folderFound) {
            try {
                folderList.add(new Folder(directory, file.getCanonicalPath(), counter));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void populateData() {
        //TODO: bring db declare + clear table out + first populate related code
        //TODO: change function to take parameter: single song to add re-usability for code above
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        db.clearAllTables();
        FolderDAO folderDAO = db.folderDAO();
        SongDAO songDAO = db.songDAO();
        ArtistDAO artistDAO = db.artistDAO();
        AlbumDAO albumDAO = db.albumDAO();

        Artist unknownArtist = new Artist("Unknown");
        unknownArtist.setArtistId(Math.toIntExact(artistDAO.insert(unknownArtist)));
        Album unknownAlbum = new Album("Unknown", 0);
        unknownAlbum.setAlbumId(Math.toIntExact(albumDAO.insert(unknownAlbum)));

        if (folderList != null)
            for (Folder f : folderList)
                folderDAO.insertAll(f);

        if (songList != null) {
            try (MediaMetadataRetriever mmr = new MediaMetadataRetriever()) {
                for (Song s : songList) {
                    mmr.setDataSource(s.getFolder().getPath() + "/" + s.getName());

                    String temporary = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    if (temporary != null)
                        s.setDuration((int) (Long.parseLong(temporary)/1000));

                    temporary = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    if (temporary != null)
                        ArtistDAO.bindArtistToSong(s, new Artist(temporary), artistDAO);
                    else
                        ArtistDAO.bindArtistToSong(s, unknownArtist, artistDAO);

                    temporary = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                    if (temporary != null) {
                        byte[] artBytes = mmr.getEmbeddedPicture();
                        if (artBytes != null)
                            AlbumDAO.bindAlbumToSong(s, new Album(temporary, 1), albumDAO);
                        else
                            AlbumDAO.bindAlbumToSong(s, new Album(temporary, 0), albumDAO);
                    } else
                        AlbumDAO.bindAlbumToSong(s, unknownAlbum, albumDAO);

                    Folder f = db.folderDAO().getFolderByPath(s.getFolder().getPath());
                    s.setsFolderId(f.getFolderId());

                    songDAO.insert(s);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("MyLog", "onRequestPermissionsResult: called");
        if (requestCode == REQUEST_PERMISSION_READING_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SplashActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();

                firstRuntimeDataPopulate();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(this::nextActivity, 2000);
            } else {
                Toast.makeText(SplashActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                SplashActivity.this.finishAffinity();
            }
        }
    }

    private void HandlePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, RequiredPermission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, RequiredPermission))
                showExplanationAboutPermission();
            else
                ActivityCompat.requestPermissions(this,
                        new String[]{SplashActivity.RequiredPermission},
                        1);
        else {
            Toast.makeText(SplashActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();

//            updateDB();
            AppDatabase db = AppDatabase.getInstance(this);
            List<SongCollection> sc = db.songCollectionDAO().getCollectionByType(SongCollection.TYPE_QUEUE);
            for (SongCollection obj : sc) {
                db.songCollectionDAO().delete(obj);
            }

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(this::nextActivity, 2000);
        }
    }

    private void showExplanationAboutPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Needed")
                .setMessage("The App need read permission to work at all.")
                .setPositiveButton(android.R.string.ok, (dialog, id) ->
                        ActivityCompat.requestPermissions(SplashActivity.this,
                                new String[]{SplashActivity.RequiredPermission}, 1))
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) ->
                        SplashActivity.this.finishAffinity());
        builder.create().show();
    }
}
