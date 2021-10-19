package com.example.movieapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.movieapp.ui.fragments.ActionFragment;
import com.example.movieapp.ui.fragments.ComedyFragment;
import com.example.movieapp.ui.fragments.DramaFragment;
import com.example.movieapp.ui.fragments.HomeFragment;
import com.example.movieapp.ui.fragments.WarFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
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
        return new ActionFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Hành động";
                break;
            case 1:
                title = "Hoạt hình";
                break;
            case 2:
                title = "Lãng mạn";
                break;
            case 3:
                title = "Cổ trang";
                break;
        }
        return title;
    }
}
