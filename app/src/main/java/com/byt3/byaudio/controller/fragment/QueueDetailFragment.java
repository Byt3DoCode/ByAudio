package com.byt3.byaudio.controller.fragment;

import static com.byt3.byaudio.utils.functions.milliSecondsToTimer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.adapter.QueueDetailRecyclerAdapter;
import com.byt3.byaudio.model.AppDatabase;
import com.byt3.byaudio.model.Song;
import com.byt3.byaudio.model.SongCollection;
import com.byt3.byaudio.model.objrelation.CollectionWithSongs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QueueDetailFragment extends Fragment {
    Context context;
    AppDatabase db;
    RecyclerView recyclerView;
    TextView sizeText, durationText;
    Button saveBtn, nameBtn;
    QueueDetailRecyclerAdapter adapter;
    List<CollectionWithSongs> queueList;
    CollectionWithSongs currentQueue;
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
        View view = inflater.inflate(R.layout.fragment_queue_detail, container, false);
        initView(view);

        adapter = new QueueDetailRecyclerAdapter(context, null, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        disposable.add(db.CoSosDAO().getCollectionWithSongsByType(SongCollection.TYPE_QUEUE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .map(list -> Collections.unmodifiableList(new ArrayList<>(list)))
                .subscribe(list -> {
                    queueList = list;
                    if (queueList.size()!=0){
                        currentQueue = queueList.get(queueList.size() - 1);
                        setupSongCollection();
                    }
                }, throwable -> {
                    throw throwable;
                }));

        nameBtn.setOnClickListener(view1 -> {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item);
            for (CollectionWithSongs cws : queueList)
                arrayAdapter.add(cws.songCollection.getScName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                currentQueue = queueList.get(which);
                setupSongCollection();
            });
            builderSingle.show();
        });

        saveBtn.setOnClickListener(view12 -> {
            currentQueue.getSongCollection().setScType(SongCollection.TYPE_PLAYLIST);
            db.CoSosDAO().update(currentQueue);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();
                Collections.swap(currentQueue.songs, fromPos, toPos);
                db.CoSosDAO().update(currentQueue);
                adapter.setListSilent(currentQueue.getSongs());
                adapter.notifyItemMoved(fromPos, toPos);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                currentQueue.songs.remove(viewHolder.getLayoutPosition());
                db.CoSosDAO().update(currentQueue);
                adapter.setListSilent(currentQueue.getSongs());
                adapter.notifyItemRangeRemoved(viewHolder.getLayoutPosition(), 1);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    private void setupSongCollection() {
        nameBtn.setText(currentQueue.getSongCollection().getScName());
        String size = currentQueue.getSongCollection().getScSize() + " songs";
        sizeText.setText(size);
        durationText.setText(milliSecondsToTimer(currentQueue.getSongCollection().getScTotalDuration()*1000L));
        for (Song s : currentQueue.getSongs()) {
            s.setAlbum(db.albumDAO().getAlbumById(s.getsAlbumId()));
            s.setArtist(db.artistDAO().getArtistById(s.getsArtistId()));
            s.setFolder(db.folderDAO().getFolderById(s.getsFolderId()));
        }
        adapter.setList(currentQueue.getSongs());
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        sizeText = view.findViewById(R.id.queueSize);
        durationText = view.findViewById(R.id.queueDuration);
        nameBtn = view.findViewById(R.id.queueNameBtn);
        saveBtn = view.findViewById(R.id.buttonSave);
    }
}
