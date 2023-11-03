package com.byt3.byaudio.controller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.adapter.SongRecyclerAdapter;
import com.byt3.byaudio.model.Song;
import com.google.android.material.search.SearchView;

import java.util.List;

public class SearchLocalFragment extends Fragment {
    Button clearBtn;
    SearchView searchView;
    RecyclerView recyclerView;
    SongRecyclerAdapter songAdapter;
    List<Song> songs;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_local, container, false);
        initView(view);
        songAdapter = new SongRecyclerAdapter(getActivity(), songs);
        recyclerView.setAdapter(songAdapter);
        return view;
    }

    private void initView(View view) {
        searchView = view.findViewById(R.id.searchBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        clearBtn = view.findViewById(R.id.clearHistoryButton);
    }
}
