package com.byt3.byaudio.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.byt3.byaudio.controller.service.PlayerService;
import com.byt3.byaudio.model.AppDatabase;
import com.byt3.byaudio.model.Folder;
import com.byt3.byaudio.model.SongCollection;
import com.byt3.byaudio.model.objrelation.CollectionWithSongs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlaylistMenuFragment extends Fragment {
    Context context;
    AppDatabase db;
    RecyclerView recyclerView;
    PlaylistRecyclerAdapter playlistAdapter;
    Button folderBtn, favoriteBtn;
    List<SongCollection> playlists;
    List<Folder> folders;
    CompositeDisposable disposable;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.db = AppDatabase.getInstance(context);
        this.disposable = new CompositeDisposable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_menu, container, false);
        initView(view);

        folders = db.folderDAO().getAll();

        playlistAdapter = new PlaylistRecyclerAdapter(context, null, getActivity());
        recyclerView.setAdapter(playlistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        favoriteBtn.setVisibility(View.GONE);

        disposable.add(db.songCollectionDAO().getCollectionByTypeRT(SongCollection.TYPE_PLAYLIST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .map(list -> Collections.unmodifiableList(new ArrayList<>(list)))
                .subscribe(list -> {
                    playlists = list;
                    if (playlists.size()!=0){
                        playlistAdapter.setList(playlists);
                    }
                }, throwable -> {
                    throw throwable;
                }));

        folderBtn.setOnClickListener(view1 -> {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item);
            for (Folder f : folders)
                arrayAdapter.add(f.getName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                Intent intent = new Intent(context, FolderDetailActivity.class);
                intent.putExtra("folder", (Parcelable) folders.get(which));
                startActivity(intent);
            });
            builderSingle.show();
        });
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        favoriteBtn = view.findViewById(R.id.btnFavorite);
        folderBtn = view.findViewById(R.id.btnFolder);
    }
}
