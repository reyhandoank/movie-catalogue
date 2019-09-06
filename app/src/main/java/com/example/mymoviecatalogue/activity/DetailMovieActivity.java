package com.example.mymoviecatalogue.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymoviecatalogue.BuildConfig;
import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.database.AppDatabase;
import com.example.mymoviecatalogue.database.MovieEntity;
import com.example.mymoviecatalogue.model.Favorite;
import com.example.mymoviecatalogue.model.GenreMovie;
import com.example.mymoviecatalogue.model.Movie;
import com.example.mymoviecatalogue.presenter.DetailMoviePresenter;
import com.example.mymoviecatalogue.presenter.FavoriteMoviePresenter;
import com.example.mymoviecatalogue.presenter.FavoritePresenter;
import com.example.mymoviecatalogue.view.DetailMovieView;
import com.example.mymoviecatalogue.view.FavoriteView;
import com.example.mymoviecatalogue.widget.FavoriteWidget;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class DetailMovieActivity extends AppCompatActivity implements DetailMovieView.View {

    private ImageView imgPoster, imgStar;
    private TextView tvTitle, tvDate, tvGenre, tvRate, tvDescription, tvOverview;
    private View line;
    private DetailMoviePresenter presenterDetail;
    private Favorite favorite;
    private FavoriteView.Movie favoriteMovie;
    private FavoriteView.Presenter favoritePresenter;
    private String id;
    private String genreList;
    StringBuilder genres;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static final String EXTRA_MOVIE = "Catalog";

    static String STATE_TITLE = "Title";
    static String STATE_DATE = "Date";
    static String STATE_GENRE = "Genre";
    static String STATE_RATE = "Rate";
    static String STATE_POSTER = "Poster";
    static String STATE_OVERVIEW = "Overview";

    private String movieTitle;
    private String movieDate;
    private String movieGenre;
    private String movieRate;
    private String moviePoster;
    private String movieOverview;

    private Menu menu;
    private MovieEntity movieEntity = new MovieEntity();
    private AppDatabase movieDatabase;
    private boolean isFavorite = false;

    public DetailMovieActivity(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getApplicationContext());
        setContentView(R.layout.activity_detail_movie);
        setTitle(getResources().getString(R.string.detail));
        imgPoster = findViewById(R.id.img_poster);
        tvTitle = findViewById(R.id.tv_title);
        tvDate = findViewById(R.id.tv_date);
        tvGenre = findViewById(R.id.tv_genre);
        tvRate = findViewById(R.id.tv_rate);
        tvDescription = findViewById(R.id.tv_description);
        imgStar = findViewById(R.id.img_star3);
        tvOverview = findViewById(R.id.overview);
        line = findViewById(R.id.line);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        movieDatabase = AppDatabase.Companion.getInstance(this);
        favoriteMovie = new FavoriteMoviePresenter(movieDatabase.movieDao());
        favorite = new Favorite();
        favoritePresenter = new FavoritePresenter();

        id = getIntent().getStringExtra(EXTRA_MOVIE);
        presenterDetail = new DetailMoviePresenter(this);

        swipeRefreshLayout.setOnRefreshListener(this::loadData);


        if (savedInstanceState == null) {
            loadData();
        }

        favorite.setId(Integer.valueOf(id));

        favoriteState();
    }

    @Override
    public void showMovies(@NotNull Movie movie) {
        try {
            movieTitle = movie.getTitle();
            movieDate = movie.getDate();
            movieRate = movie.getRate();
            moviePoster = movie.getPoster();
            movieOverview = movie.getDescription();

            tvTitle.setText(movieTitle);
            tvDate.setText(movieDate);
            tvRate.setText(movieRate);
            tvDescription.setText(movieOverview);
            Glide.with(this)
                    .load(BuildConfig.POSTER_URL + moviePoster)
                    .into(imgPoster);
            Glide.with(this)
                    .load(R.drawable.ic_star)
                    .apply(new RequestOptions().override(24, 24))
                    .into(imgStar);
            tvOverview.setText(R.string.overview);

            tvTitle.setAnimation(AnimationUtils.loadAnimation(this, R.anim.item_fall_down));
            tvDate.setAnimation(AnimationUtils.loadAnimation(this, R.anim.item_fall_down));
            tvRate.setAnimation(AnimationUtils.loadAnimation(this, R.anim.item_fall_down));
            tvDescription.setAnimation(AnimationUtils.loadAnimation(this, R.anim.item_rise_up));
            imgPoster.setAnimation(AnimationUtils.loadAnimation(this, R.anim.item_fall_down));
            imgStar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.item_fall_down));
            tvOverview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.item_rise_up));
            line.setAnimation(AnimationUtils.loadAnimation(this, R.anim.item_animation));
        } catch (Exception e) {
            e.printStackTrace();
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
        Toast.makeText(this, R.string.load_failed, Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        String language;
        switch (Locale.getDefault().getLanguage()) {
            case "en":
                language = "en";
                presenterDetail.getMovieDetail(BuildConfig.API_KEY, language, id);
                break;
            case "in":
                language = "id";
                presenterDetail.getMovieDetail(BuildConfig.API_KEY, language, id);
                break;
        }
    }

    @Override
    public void showGenre(@NotNull ArrayList<GenreMovie> genre) {
        genreList = "";
        genres = new StringBuilder(genreList);
        for (int i = 0; i < genre.size(); i++) {
            genres.append(genre.get(i).getGenre()).append(". ");
        }
        genreList = genres.toString();
        tvGenre.setText(genres);
        tvGenre.setAnimation(AnimationUtils.loadAnimation(this, R.anim.item_fall_down));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_TITLE, movieTitle);
        outState.putString(STATE_DATE, movieDate);
        outState.putString(STATE_GENRE, genreList);
        outState.putString(STATE_RATE, movieRate);
        outState.putString(STATE_POSTER, moviePoster);
        outState.putString(STATE_OVERVIEW, movieOverview);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieTitle = savedInstanceState.getString(STATE_TITLE);
        movieDate = savedInstanceState.getString(STATE_DATE);
        genreList = savedInstanceState.getString(STATE_GENRE);
        movieRate = savedInstanceState.getString(STATE_RATE);
        moviePoster = savedInstanceState.getString(STATE_POSTER);
        movieOverview = savedInstanceState.getString(STATE_OVERVIEW);

        tvTitle.setText(movieTitle);
        tvDate.setText(movieDate);
        tvGenre.setText(genreList);
        tvRate.setText(movieRate);
        tvDescription.setText(movieOverview);
        Glide.with(this)
                .load(BuildConfig.POSTER_URL + moviePoster)
                .into(imgPoster);
    }

    private void addToFavorite() {
        movieEntity.setMovieId(id);
        movieEntity.setMovieTitle(movieTitle);
        movieEntity.setMovieDate(movieDate);
        movieEntity.setMovieGenre(movieGenre);
        movieEntity.setMovieRate(movieRate);
        movieEntity.setMoviePoster(moviePoster);
        movieEntity.setMovieOverview(movieOverview);

        favoriteMovie.addMovie(movieEntity);

        favorite.setTitle(movieTitle);
        favorite.setDate(movieDate);
        favorite.setPoster(moviePoster);
        favorite.setRate(movieRate);

        favoritePresenter.addFavorite(this, favorite);

        sendUpdateFavorite(this);

        Toast.makeText(this, R.string.added_favorite, Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorite() {
        favoriteMovie.deleteMovie(id);
        favoritePresenter.deleteFavorite(this, favorite);

        sendUpdateFavorite(this);
        Toast.makeText(this, R.string.deleted_favorite, Toast.LENGTH_SHORT).show();
    }

    private void setFavorite() {
        if (isFavorite) {
            menu.getItem(0).setIcon(R.drawable.ic_star_white);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_star_border_white);
        }
    }

    private void favoriteState() {
        MovieEntity[] favorite = movieDatabase.movieDao().getMovieById(id);
        if (favorite.length > 0) {
            isFavorite = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        this.menu = menu;
        setFavorite();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_favorite) {
            if (isFavorite) {
                removeFromFavorite();
            } else {
                addToFavorite();
            }
            isFavorite = !isFavorite;
            setFavorite();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendUpdateFavorite(Context context) {
        Intent intent = new Intent(context, FavoriteWidget.class);
        intent.setAction(FavoriteWidget.UPDATE_WIDGET);
        context.sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenterDetail.onDestroy();
        favoritePresenter.onDestroy();
    }
}
