package com.example.mymoviecatalogue.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.adapter.FavoriteMovieAdapter;
import com.example.mymoviecatalogue.database.AppDatabase;
import com.example.mymoviecatalogue.database.MovieEntity;

import java.util.ArrayList;
import java.util.Arrays;

public class FavoriteMovieFragment extends Fragment {

    private FavoriteMovieAdapter adapter;
    private RecyclerView rvFavorite;
    private ArrayList<MovieEntity> movieList;
    private AppDatabase movieDatabase;
    private TextView txtNoData;

    public FavoriteMovieFragment() {
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

        movieDatabase = AppDatabase.Companion.getInstance(view.getContext());
        movieList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rvFavorite.setHasFixedSize(true);
        rvFavorite.setLayoutManager(layoutManager);
        adapter = new FavoriteMovieAdapter(getContext());

        showMovie();
    }

    private void showMovie() {
        movieList.clear();
        movieList.addAll(Arrays.asList(movieDatabase.movieDao().getAllMovie()));
        adapter.notifyDataSetChanged();
        adapter.setMovies(movieList);

        if (adapter.getItemCount() > 0) {
            rvFavorite.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
        } else {
            rvFavorite.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        }
        rvFavorite.setAdapter(adapter);
        txtNoData.setText(R.string.no_favorite_movies);
    }

    @Override
    public void onResume() {
        super.onResume();
        showMovie();
    }
}
