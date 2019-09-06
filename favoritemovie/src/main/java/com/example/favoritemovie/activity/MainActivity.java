package com.example.favoritemovie.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.favoritemovie.R;
import com.example.favoritemovie.adapter.MovieAdapter;
import com.example.favoritemovie.model.Movie;
import com.example.favoritemovie.presenter.FavoriteMoviePresenter;
import com.example.favoritemovie.view.FavoriteView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FavoriteView.View {

    private RecyclerView rvMovies;
    private MovieAdapter adapter;
    private ArrayList<Movie> movies;
    private FavoriteView.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovies = findViewById(R.id.rv_movies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setHasFixedSize(true);

        movies = new ArrayList<>();

        adapter = new MovieAdapter(this);

        presenter = new FavoriteMoviePresenter(this);
        presenter.fetchFavoriteMovie(this);

    }

    @Override
    public void showFavoriteMovie(@NotNull List<Movie> movie) {
        movies.clear();
        movies.addAll(movie);
        adapter.setMovies(movies);
        rvMovies.setAdapter(adapter);
    }

    @Override
    public void errorHandler(@Nullable String throwable) {

    }


}
