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
import com.example.mymoviecatalogue.adapter.TVShowAdapter;
import com.example.mymoviecatalogue.model.TvShow;
import com.example.mymoviecatalogue.view.TvShowView;
import com.example.mymoviecatalogue.viewmodel.TvShowViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class TVShowFragment extends Fragment implements TvShowView.View {

    private TVShowAdapter adapter;
    private RecyclerView rvTVShows;
    private TvShowViewModel tvShowViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page = 1;
    private int allPages = 0;
    private boolean isLoading = false;

    public TVShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
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
        adapter = new TVShowAdapter(getContext());

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        rvTVShows = view.findViewById(R.id.rv_tv_show);
        rvTVShows.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvTVShows.setHasFixedSize(true);
        tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.getTvShowList().observe(this, getTvShow);

        swipeRefreshLayout.setOnRefreshListener(this::loadData);
    }

    private void loadData() {
        String language;
        switch (Locale.getDefault().getLanguage()) {
            case "en":
                language = "en";
                tvShowViewModel.setTvShowList(BuildConfig.API_KEY, page, language, this);
                break;
            case "in":
                language = "id";
                tvShowViewModel.setTvShowList(BuildConfig.API_KEY, page, language, this);
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
    public void errorHandler(@NotNull Throwable e) {
        Toast.makeText(getContext(), R.string.load_failed, Toast.LENGTH_SHORT).show();
    }

    private Observer<ArrayList<TvShow>> getTvShow = new Observer<ArrayList<TvShow>>() {
        @Override
        public void onChanged(ArrayList<TvShow> tvshows) {
            if (tvshows != null) {
                if (page == 1) {
                    adapter.setTVShows(tvshows);
                    rvTVShows.setAdapter(adapter);
                } else {
                    adapter.refreshAdapter(tvshows);
                }
            }
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String SAVED_STATE = "Orientation";
        outState.putString(SAVED_STATE, "saved");
    }

    private void scrollListener() {
        rvTVShows.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
