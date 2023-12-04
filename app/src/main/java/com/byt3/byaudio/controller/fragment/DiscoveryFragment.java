package com.byt3.byaudio.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.adapter.AlbumViewPagerAdapter;
import com.byt3.byaudio.controller.adapter.CloudSongRecyclerAdapter;
import com.byt3.byaudio.model.Album;
import com.byt3.byaudio.model.CloudSong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscoveryFragment extends Fragment {
    Context context;
    DatabaseReference myRef;
    SearchView searchView;
    RecyclerView recyclerView;
    CloudSongRecyclerAdapter recyclerAdapter;
    ViewPager2 viewPager2;
    AlbumViewPagerAdapter viewPagerAdapter;
    List<CloudSong> list;
    Set<Album> set;
    final Handler handler = new Handler();
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (set == null || set.isEmpty())
                return;
            if (viewPager2.getCurrentItem() == set.size() - 1) {
                viewPager2.setCurrentItem(0);
                return;
            }
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);
        initView(view);

        viewPagerAdapter = new AlbumViewPagerAdapter(context, null);
        recyclerAdapter = new CloudSongRecyclerAdapter(context, null);

        recyclerView.setAdapter(recyclerAdapter);
        viewPager2.setAdapter(viewPagerAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 4000);
            }
        });

        myRef = FirebaseDatabase.getInstance("https://streammusic-7ed2d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("/songs");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                set = new HashSet<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    System.out.println(dataSnapshot.toString());
                    CloudSong song = dataSnapshot.getValue(CloudSong.class);
                    assert song != null;
                    Album album = new Album(song.getAlbum(), song.getCover());
                    list.add(0, song);
                    set.add(album);
                }
                recyclerAdapter.setList(list);
                viewPagerAdapter.setList(new ArrayList<>(set));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                searchCloud(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.trim().equals("") && newText.length() >= 3)
                    searchCloud(newText);
                return true;
            }
        });


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 4000);
    }

    private void searchCloud(String text) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                    CloudSong song = dataSnapshot1.getValue(CloudSong.class);
                    assert song != null;
                    if (text.equals(""))
                        list.add(0, song);
                    else if (song.getName().toLowerCase().trim().contains(text.toLowerCase().trim()))
                        list.add(0, song);
                }
                recyclerAdapter.setList(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);
        viewPager2 = view.findViewById(R.id.viewpager);
    }
}
