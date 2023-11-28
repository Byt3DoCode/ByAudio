package com.byt3.byaudio.controller.fragment;

import static com.byt3.byaudio.utils.functions.milliSecondsToTimer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.byt3.byaudio.controller.service.PlayerService;
import com.byt3.byaudio.model.AppDatabase;
import com.byt3.byaudio.model.CollectionSongCrossRef;
import com.byt3.byaudio.model.Song;
import com.byt3.byaudio.model.SongCollection;
import com.byt3.byaudio.model.objrelation.CollectionWithSongs;

import java.util.Arrays;
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
    PlayerService playerService;
    boolean isServiceConnected;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayerService.MyBinder binder = (PlayerService.MyBinder) iBinder;
            playerService = binder.getplayerService();
            isServiceConnected = true;
            setupSongCollection();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceConnected = false;
        }
    };

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
                .subscribe(list -> {
                    queueList = list;
                    if (queueList.size()!=0){
                        currentQueue = queueList.get(queueList.size() - 1);
                        Intent serviceIntent = new Intent(context, PlayerService.class);
                        context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
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
            String name = currentQueue.getSongCollection().getScName();
            if (name.contains("Playlist")) {
                Toast.makeText(context, "Already saved", Toast.LENGTH_SHORT).show();
            } else {
                String newName = currentQueue.getSongCollection().getScName();
                newName = newName.replace("Queue ", "");
                newName = newName.replace("Search", "Playlist");
                newName = newName.replace("Folder", "Playlist");
                currentQueue.getSongCollection().setScType(SongCollection.TYPE_PLAYLIST);
                currentQueue.getSongCollection().setScName(newName);
                db.songCollectionDAO().updateCollection(currentQueue.getSongCollection());
                Toast.makeText(context, "Saving queue", Toast.LENGTH_SHORT).show();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int queuePos = queueList.indexOf(currentQueue);
                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();
                CollectionSongCrossRef fromCrossRef = currentQueue.getCrossRefs().get(fromPos);
                CollectionSongCrossRef toCrossRef = currentQueue.getCrossRefs().get(toPos);
                int fromIndex = fromCrossRef.getCrSongOrder();
                int toIndex = toCrossRef.getCrSongOrder();
                fromCrossRef.setCrSongOrder(-1);
                toCrossRef.setCrSongOrder(fromIndex);
                db.CoSosDAO().updateCollectionSongCrossRef(fromCrossRef);
                db.CoSosDAO().updateCollectionSongCrossRef(toCrossRef);
                fromCrossRef.setCrSongOrder(toIndex);
                db.CoSosDAO().updateCollectionSongCrossRef(fromCrossRef);
                Collections.swap(currentQueue.songs, fromPos, toPos);
                Collections.swap(currentQueue.crossRefs, fromPos, toPos);
                adapter.setListSilent(currentQueue.getSongs());
                adapter.notifyItemMoved(fromPos, toPos);
                queueList.set(queuePos, currentQueue);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int queuePos = queueList.indexOf(currentQueue);
                CollectionSongCrossRef crossRef = new CollectionSongCrossRef(
                        currentQueue.getSongCollection().getScId(),
                        currentQueue.getSongs()
                                .get(viewHolder.getLayoutPosition())
                                .getSongId(),
                        viewHolder.getLayoutPosition());
                db.CoSosDAO().deleteCrossRef(crossRef);
                currentQueue.songs.remove(viewHolder.getLayoutPosition());
                currentQueue.crossRefs.remove(viewHolder.getLayoutPosition());
                adapter.setListSilent(currentQueue.getSongs());
                adapter.notifyItemRangeRemoved(viewHolder.getLayoutPosition(), 1);
                queueList.set(queuePos, currentQueue);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
        if (isServiceConnected){
            context.unbindService(serviceConnection);
            isServiceConnected = false;
        }
    }

    private void setupSongCollection() {
        nameBtn.setText(currentQueue.getSongCollection().getScName());
        String size = currentQueue.getSongCollection().getScSize() + " songs";
        sizeText.setText(size);
        durationText.setText(milliSecondsToTimer(currentQueue.getSongCollection().getScTotalDuration()*1000L));
        Song[] list = new Song[currentQueue.getSongCollection().getScSize()];
        for (int i = 0; i < currentQueue.getCrossRefs().size(); ++i) {
            Song s = currentQueue.getSongs().get(i);
            s.setAlbum(db.albumDAO().getAlbumById(s.getsAlbumId()));
            s.setArtist(db.artistDAO().getArtistById(s.getsArtistId()));
            s.setFolder(db.folderDAO().getFolderById(s.getsFolderId()));
            list[currentQueue.getCrossRefs().get(i).getCrSongOrder()] = s;
        }
        adapter.setList(Arrays.asList(list));
        adapter.setIndex(playerService.getIndex());
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        sizeText = view.findViewById(R.id.queueSize);
        durationText = view.findViewById(R.id.queueDuration);
        nameBtn = view.findViewById(R.id.queueNameBtn);
        saveBtn = view.findViewById(R.id.buttonSave);
    }
}
