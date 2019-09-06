package com.example.mymoviecatalogue.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.adapter.FavoriteTVShowAdapter;
import com.example.mymoviecatalogue.database.AppDatabase;
import com.example.mymoviecatalogue.database.TvShowEntity;

import java.util.ArrayList;
import java.util.Arrays;

public class FavoriteTvShowFragment extends Fragment {

    private FavoriteTVShowAdapter adapter;
    private RecyclerView rvFavorite;
    private ArrayList<TvShowEntity> tvshowList;
    private AppDatabase tvshowDatabase;
    private TextView txtNoData;

    public FavoriteTvShowFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavorite = view.findViewById(R.id.rv_favorite);
        txtNoData = view.findViewById(R.id.txt_no_data);

        tvshowDatabase = AppDatabase.Companion.getInstance(view.getContext());
        tvshowList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rvFavorite.setHasFixedSize(true);
        rvFavorite.setLayoutManager(layoutManager);
        adapter = new FavoriteTVShowAdapter(getContext());

        showTvShow();
    }

    private void showTvShow() {
        tvshowList.clear();
        tvshowList.addAll(Arrays.asList(tvshowDatabase.tvshowDao().getAllTvShow()));
        adapter.notifyDataSetChanged();
        adapter.setTvShows(tvshowList);

        if (adapter.getItemCount() > 0) {
            rvFavorite.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
        } else {
            rvFavorite.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        }
        rvFavorite.setAdapter(adapter);
        txtNoData.setText(R.string.no_favorite_tv_shows);
    }

    @Override
    public void onResume() {
        super.onResume();
        showTvShow();
    }
}
