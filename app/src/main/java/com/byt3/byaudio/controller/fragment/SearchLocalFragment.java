package com.byt3.byaudio.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.adapter.SongRecyclerAdapter;
import com.byt3.byaudio.model.AppDatabase;
import com.byt3.byaudio.model.Song;

import java.util.List;

public class SearchLocalFragment extends Fragment {
    Context context;
    Button clearBtn;
    SearchView searchView;
    RecyclerView recyclerView;
    SongRecyclerAdapter songAdapter;
    List<Song> songs;
    AppDatabase db;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.db = AppDatabase.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_local, container, false);
        initView(view);
        songAdapter = new SongRecyclerAdapter(context, songs, getActivity());
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                List<SongAndArtistAndAlbumAndFolder> list = db.saafDAO().getSAAFBySongName("%"+query+"%");
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                searchSong(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.trim().equals("") && newText.length() >= 3)
                    searchSong(newText);
                return true;
            }
        });
        return view;
    }
    private void searchSong(String text) {
        songs = db.songDAO().getSongLikeName("%"+text+"%");
        for (Song s : songs) {
            s.setArtist(db.artistDAO().getArtistById(s.sArtistId));
            s.setAlbum(db.albumDAO().getAlbumById(s.sAlbumId));
            s.setFolder(db.folderDAO().getFolderById(s.sFolderId));
        }
        songAdapter.setQueryText(text);
        songAdapter.setList(songs);
    }
    private void initView(View view) {
        searchView = view.findViewById(R.id.searchBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        clearBtn = view.findViewById(R.id.clearHistoryButton);
    }
}
