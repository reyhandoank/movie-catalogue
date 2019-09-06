package com.example.mymoviecatalogue.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.example.mymoviecatalogue.BuildConfig;
import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.adapter.MovieAdapter;
import com.example.mymoviecatalogue.adapter.TVShowAdapter;
import com.example.mymoviecatalogue.fragment.FavoriteFragment;
import com.example.mymoviecatalogue.fragment.MovieFragment;
import com.example.mymoviecatalogue.fragment.TVShowFragment;
import com.example.mymoviecatalogue.model.Movie;
import com.example.mymoviecatalogue.model.MovieResponse;
import com.example.mymoviecatalogue.model.TvShow;
import com.example.mymoviecatalogue.model.TvShowResponse;
import com.example.mymoviecatalogue.view.SearchItemView;
import com.example.mymoviecatalogue.viewmodel.SearchViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SearchItemView.View, BottomNavigationView.OnNavigationItemSelectedListener {

    private SearchViewModel searchViewModel;
    private SearchView searchView;
    private MovieAdapter movieAdapter;
    private TVShowAdapter tvShowAdapter;
    private RecyclerView rvMain;
    private FrameLayout container;
    private BottomNavigationView navView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int page = 1;
    private int allPages = 1;
    private boolean isLoading = false;
    private String SAVE_VIEW = "save_view";
    private String MENU_CONDITION = "menu_condition";
    private String SEARCH_QUERY = "search_query";
    private boolean tvShowSelected;
    private String query;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_movie:
                        setTitle(getResources().getString(R.string.title_movie));
                        fragment = new MovieFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                        tvShowSelected = false;
                        return true;
                    case R.id.navigation_tv_show:
                        setTitle(getResources().getString(R.string.title_tv_show));
                        fragment = new TVShowFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                        tvShowSelected = true;
                        return true;
                    case R.id.navigation_favorite:
                        setTitle(R.string.favorite);
                        fragment = new FavoriteFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        container = findViewById(R.id.container_layout);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        rvMain = findViewById(R.id.rv_main);

        prepare();
        scrollListener();

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_movie);
        } else {
            if (savedInstanceState.getInt(SAVE_VIEW) == View.VISIBLE) {
                tvShowSelected = savedInstanceState.getBoolean(MENU_CONDITION);
                if (tvShowSelected) {
                    tvShowPrepare();
                } else {
                    moviePrepare();
                }

                swipeRefreshLayout.setVisibility(View.VISIBLE);
                navView.setVisibility(View.GONE);
                container.setVisibility(View.GONE);
                query = savedInstanceState.getString(SEARCH_QUERY);
            }
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (tvShowSelected) {
                tvshowData(query);
            } else {
                movieData(query);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search));

        if (swipeRefreshLayout.getVisibility() == View.VISIBLE) {
            menuItem.expandActionView();
            searchView.setQuery(query, true);
            searchView.clearFocus();
        }

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {

            private Timer timer;
            private long DELAY = 1000;
            @Override
            public boolean onQueryTextSubmit(String loadQuery) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                moviePrepare();
                tvShowPrepare();
                if (newText != null) {
                    query = newText;
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> {
                                if(tvShowSelected) {
                                    tvShowAdapter.clearMovie();
                                    tvshowData(query);
                                } else {
                                    movieAdapter.clearTvShow();
                                    movieData(query);
                                }
                            });
                        }
                    }, DELAY);
                }
                return true;
            }
        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                navView.setVisibility(View.GONE);
                container.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                if (tvShowSelected) {
                    tvShowAdapter.clearMovie();
                } else {
                    movieAdapter.clearTvShow();
                }

                swipeRefreshLayout.setVisibility(View.GONE);
                searchView.onActionViewCollapsed();
                navView.setVisibility(View.VISIBLE);
                container.setVisibility(View.VISIBLE);
                return true;
            }
        });

        return true;
    }

    public void movieData(String query) {
        String language;
        switch (Locale.getDefault().getLanguage()) {
            case "en":
                language = "en";
                searchViewModel.searchMovie(BuildConfig.API_KEY, query, page, language, this);
                break;
            case "in":
                language = "id";
                searchViewModel.searchMovie(BuildConfig.API_KEY, query, page, language, this);
                break;
        }
    }

    public void tvshowData(String query) {
        String language;
        switch (Locale.getDefault().getLanguage()) {
            case "en":
                language = "en";
                searchViewModel.searchTvShow(BuildConfig.API_KEY, query, page, language, this);
                break;
            case "in":
                language = "id";
                searchViewModel.searchTvShow(BuildConfig.API_KEY, query, page, language, this);
                break;
        }
    }

    private Observer<ArrayList<Movie>> getMovie = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if (movies != null) {
                if (page == 1) {
                    movieAdapter.setMovies(movies);
                    rvMain.setAdapter(movieAdapter);
                } else {
                    movieAdapter.refreshAdapter(movies);
                }

                if (movieAdapter.getItemCount() > 0) {
                    rvMain.setVisibility(View.VISIBLE);

                } else {
                    rvMain.setVisibility(View.GONE);
                }
            }
        }
    };

    private Observer<ArrayList<TvShow>> getTvShow = new Observer<ArrayList<TvShow>>() {
        @Override
        public void onChanged(ArrayList<TvShow> tvshows) {
            if (tvshows != null) {
                if (page == 1) {
                    tvShowAdapter.setTVShows(tvshows);
                    rvMain.setAdapter(tvShowAdapter);
                } else {
                    tvShowAdapter.refreshAdapter(tvshows);
                }

                if (tvShowAdapter.getItemCount() > 0) {
                    rvMain.setVisibility(View.VISIBLE);
                } else {
                    rvMain.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepare() {
        AndroidNetworking.initialize(this);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        rvMain.setHasFixedSize(true);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
    }

    private void moviePrepare() {
        movieAdapter = new MovieAdapter(this);
        searchViewModel.getMovieList().observe(this, getMovie);
        movieAdapter.notifyDataSetChanged();
        rvMain.setAdapter(movieAdapter);
    }

    private void tvShowPrepare() {
        tvShowAdapter = new TVShowAdapter(this);
        searchViewModel.getTvShowList().observe(this, getTvShow);
        tvShowAdapter.notifyDataSetChanged();
        rvMain.setAdapter(tvShowAdapter);
    }

    @Override
    public void getTvShowData(@NotNull TvShowResponse tvshow) {
        allPages = tvshow.getTotal_pages();
    }

    @Override
    public void getMovieData(@NotNull MovieResponse movies) {
        allPages = movies.getTotal_pages();
    }

    @Override
    public void showProgressBar() {
        isLoading = true;
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void errorHandler(@Nullable Throwable t) {
        Toast.makeText(this, R.string.not_found, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    private void scrollListener() {
        rvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    movieData(query);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String STATE_SAVED = "state_saved";
        outState.putString(STATE_SAVED, "saved");
        outState.putInt(SAVE_VIEW, swipeRefreshLayout.getVisibility());
        outState.putBoolean(MENU_CONDITION, tvShowSelected);
        outState.putString(SEARCH_QUERY, searchView.getQuery().toString());
    }


}
