package com.byt3.byaudio.controller;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.adapter.SongRecyclerAdapter;
import com.byt3.byaudio.model.AppDatabase;
import com.byt3.byaudio.model.CollectionSongCrossRef;
import com.byt3.byaudio.model.Folder;
import com.byt3.byaudio.model.Song;
import com.byt3.byaudio.model.SongCollection;
import com.byt3.byaudio.model.objrelation.CollectionWithSongs;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FolderDetailActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SongRecyclerAdapter adapter;
    TextView folderSize;
    Toolbar toolbar;
    Folder folder;
    SongCollection songCollection;
    List<Song> songs;
    AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_detail);
        toolbar = findViewById(R.id.toolBar);
        folderSize = findViewById(R.id.folderSizeText);
        recyclerView = findViewById(R.id.recyclerView);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        db = AppDatabase.getInstance(this);
        adapter = new SongRecyclerAdapter(this, null, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        if (bundle.containsKey("folder")) {
            folder = bundle.getParcelable("folder");
            assert folder != null;
            toolbar.setTitle(folder.getName());
            adapter.setListName("Folder " + folder.getName());
            String size = folder.getSize() + " songs";
            folderSize.setText(size);
            songs = db.songDAO().getSongByFolder(folder.getFolderId());
            for (Song s : songs) {
                s.setAlbum(db.albumDAO().getAlbumById(s.getsAlbumId()));
                s.setArtist(db.artistDAO().getArtistById(s.getsArtistId()));
                s.setFolder(db.folderDAO().getFolderById(s.getsFolderId()));
            }
        } else if (bundle.containsKey("playlist")) {
            songCollection = bundle.getParcelable("playlist");
            assert songCollection != null;
            toolbar.setTitle(songCollection.getScName());
            adapter.setListName(songCollection.getScName());
            String size = songCollection.getScSize() + " songs";
            folderSize.setText(size);
            CollectionWithSongs collection = db.CoSosDAO().getCollectionWithSongsByscId(songCollection.getScId());
            Song[] list = new Song[collection.getSongCollection().getScSize()];
            for (int i = 0; i < collection.getCrossRefs().size(); ++i) {
                Song s = collection.getSongs().get(i);
                s.setAlbum(db.albumDAO().getAlbumById(s.getsAlbumId()));
                s.setArtist(db.artistDAO().getArtistById(s.getsArtistId()));
                s.setFolder(db.folderDAO().getFolderById(s.getsFolderId()));
                list[collection.getCrossRefs().get(i).getCrSongOrder()] = s;
            }
            songs = Arrays.asList(list);
        } else
            finish();
        adapter.setList(songs);
    }
}
