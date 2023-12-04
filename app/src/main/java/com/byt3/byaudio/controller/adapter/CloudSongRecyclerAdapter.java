package com.byt3.byaudio.controller.adapter;

import static com.byt3.byaudio.utils.functions.FIREBASE_STORAGE;
import static com.byt3.byaudio.utils.functions.milliSecondsToTimer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.service.PlayerService;
import com.byt3.byaudio.controller.viewholder.SongViewHolder;
import com.byt3.byaudio.model.Album;
import com.byt3.byaudio.model.Artist;
import com.byt3.byaudio.model.CloudSong;
import com.byt3.byaudio.model.Folder;
import com.byt3.byaudio.model.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CloudSongRecyclerAdapter extends RecyclerView.Adapter<SongViewHolder> {
    private final Context context;
    private List<CloudSong> list;

    public CloudSongRecyclerAdapter(Context context, List<CloudSong> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<CloudSong> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        CloudSong song = list.get(position);
        holder.optionButton.setVisibility(View.GONE);
        holder.songName.setText(song.getName());
        holder.artistName.setText(song.getArtist());
        holder.songLength.setText(milliSecondsToTimer(song.getDuration() * 1000L));
        Picasso.get().load(song.getCover()).into(holder.albumCover);
        holder.itemView.setOnClickListener(view -> {
            List<Song> songs = new ArrayList<>();
            Song s = new Song(song.getName(), song.getDuration());
            s.setArtist(new Artist(song.getArtist()));
            s.setFolder(new Folder(FIREBASE_STORAGE, song.getUrl()));
            s.setAlbum(new Album(2, song.getCover()));
            songs.add(0, s);
            DatabaseReference myRef = FirebaseDatabase.getInstance("https://streammusic-7ed2d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("/songs");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        System.out.println(dataSnapshot.toString());
                        CloudSong cloudsong = dataSnapshot.getValue(CloudSong.class);
                        assert cloudsong != null;
                        if (!cloudsong.getAlbum().equals(song.getAlbum()) || cloudsong.getUrl().equals(song.getUrl()))
                            continue;
                        Song s = new Song(cloudsong.getName(), cloudsong.getDuration());
                        s.setArtist(new Artist(cloudsong.getArtist()));
                        s.setFolder(new Folder(FIREBASE_STORAGE, cloudsong.getUrl()));
                        s.setAlbum(new Album(2, cloudsong.getCover()));
                        songs.add(0, s);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(context, PlayerService.class);
            intent.putExtra("newList", true);
            intent.putParcelableArrayListExtra("songs", new ArrayList<>(songs));
            intent.putExtra("index", 0);
            context.startService(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }
}
