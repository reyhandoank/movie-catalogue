package com.example.mymoviecatalogue.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mymoviecatalogue.BuildConfig;
import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.adapter.MovieAdapter;
import com.example.mymoviecatalogue.model.Movie;
import com.example.mymoviecatalogue.view.MovieView;
import com.example.mymoviecatalogue.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements MovieView.View {

    private MovieAdapter adapter;
    private RecyclerView rvMovies;
    private MovieViewModel movieViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int page = 1;
    private int allPages = 0;
    private boolean isLoading = false;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepare(view);
        scrollListener();
        if (savedInstanceState == null) {
            loadData();
        }
    }

    private void prepare(View view) {
        adapter = new MovieAdapter(getContext());

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovieList().observe(this, getMovie);
        rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvMovies.setHasFixedSize(true);

        swipeRefreshLayout.setOnRefreshListener(this::loadData);
    }

    private void loadData() {
        String language;
        switch (Locale.getDefault().getLanguage()) {
            case "en":
                language = "en";
                movieViewModel.setMovieList(BuildConfig.API_KEY, page, language, this);
                break;
            case "in":
                language = "id";
                movieViewModel.setMovieList(BuildConfig.API_KEY, page, language, this);
                break;
        }
    }

    @Override
    public void showProgressBar() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void errorHandler(@NonNull Throwable e) {
        Toast.makeText(getContext(), R.string.load_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String SAVED_STATE = "Orientation";
        outState.putString(SAVED_STATE, "saved");
    }

    private Observer<ArrayList<Movie>> getMovie = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if (movies != null) {
                if (page == 1) {
                    adapter.setMovies(movies);
                    rvMovies.setAdapter(adapter);
                } else {
                    adapter.refreshAdapter(movies);
                }
            }
        }
    };

    private void scrollListener() {
        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert linearLayoutManager != null;
                int countItem = linearLayoutManager.getItemCount();
                int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                boolean isLastPosition = countItem - 1 == lastPosition;
                if(!isLoading && isLastPosition && page != allPages) {
                    page = page + 1;
                    loadData();
                }
            }
        });
    }
}
