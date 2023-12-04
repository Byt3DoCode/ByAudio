package com.byt3.byaudio.controller.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.byt3.byaudio.controller.fragment.DiscoveryFragment;
import com.byt3.byaudio.controller.fragment.PlayerFragment;
import com.byt3.byaudio.controller.fragment.PlaylistMenuFragment;
import com.byt3.byaudio.controller.fragment.QueueDetailFragment;
import com.byt3.byaudio.controller.fragment.SearchLocalFragment;

public class FragmentViewPagerAdapter extends FragmentStateAdapter {
    int pageNumber;

    public FragmentViewPagerAdapter(FragmentActivity fa, int pageNumber) {
        super(fa);
        this.pageNumber = pageNumber;
    }

    @Override
    public int getItemCount() {
        return pageNumber;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new DiscoveryFragment();
            case 1:
                return new PlayerFragment();
            case 2:
                return new QueueDetailFragment();
            case 3:
                return new SearchLocalFragment();
            case 4:
                return new PlaylistMenuFragment();
        }
        return new DiscoveryFragment();
    }
}
