package com.byt3.byaudio.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.FolderDetailActivity;
import com.byt3.byaudio.controller.adapter.PlaylistRecyclerAdapter;
import com.byt3.byaudio.model.AppDatabase;
import com.byt3.byaudio.model.Folder;
import com.byt3.byaudio.model.SongCollection;

import java.util.List;

public class PlaylistMenuFragment extends Fragment {
    Context context;
    AppDatabase db;
    RecyclerView recyclerView;
    PlaylistRecyclerAdapter playlistAdapter;
    Button folderBtn, favoriteBtn;
    List<SongCollection> playlists;
    List<Folder> folders;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.db = AppDatabase.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_menu, container, false);
        initView(view);

        playlists = db.songCollectionDAO().getCollectionByType(SongCollection.TYPE_PLAYLIST);
        folders = db.folderDAO().getAll();

        playlistAdapter = new PlaylistRecyclerAdapter(context, playlists, getActivity());
        recyclerView.setAdapter(playlistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        favoriteBtn.setVisibility(View.GONE);

        folderBtn.setOnClickListener(view1 -> {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item);
            for (Folder f : folders)
                arrayAdapter.add(f.getName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                Intent intent = new Intent(context, FolderDetailActivity.class);
                intent.putExtra("folder", folders.get(which));
                startActivity(intent);
            });
            builderSingle.show();
        });
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        favoriteBtn = view.findViewById(R.id.btnFavorite);
        folderBtn = view.findViewById(R.id.btnFolder);
    }
}
