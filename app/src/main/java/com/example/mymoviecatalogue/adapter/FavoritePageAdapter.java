package com.example.mymoviecatalogue.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mymoviecatalogue.fragment.FavoriteMovieFragment;
import com.example.mymoviecatalogue.fragment.FavoriteTvShowFragment;

public class FavoritePageAdapter extends FragmentStatePagerAdapter {

    private Fragment[] pages = {
            new FavoriteMovieFragment(), new FavoriteTvShowFragment()
    };

    public FavoritePageAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {
        return pages[position];
    }

    @Override
    public int getCount() {
        return pages.length;
    }
}
