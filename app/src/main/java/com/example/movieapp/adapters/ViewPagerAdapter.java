package com.example.movieapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.movieapp.ui.fragments.ActionFragment;
import com.example.movieapp.ui.fragments.ComedyFragment;
import com.example.movieapp.ui.fragments.DramaFragment;
import com.example.movieapp.ui.fragments.HomeFragment;
import com.example.movieapp.ui.fragments.WarFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ActionFragment();
            case 1:
                return new ComedyFragment();
            case 2:
                return new DramaFragment();
            case 3:
                return new WarFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}