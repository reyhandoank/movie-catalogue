package com.example.mymoviecatalogue.activity;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymoviecatalogue.BuildConfig;
import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.database.AppDatabase;
import com.example.mymoviecatalogue.database.TvShowEntity;
import com.example.mymoviecatalogue.model.GenreTvShow;
import com.example.mymoviecatalogue.model.TvShow;
import com.example.mymoviecatalogue.presenter.DetailTvShowPresenter;
import com.example.mymoviecatalogue.presenter.FavoriteTvShowPresenter;
import com.example.mymoviecatalogue.view.DetailTvShowView;
import com.example.mymoviecatalogue.view.FavoriteView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class DetailTVShowActivity extends AppCompatActivity implements DetailTvShowView.View {

    private ImageView imgPoster, imgStar;
    private TextView tvTitle, tvDate, tvGenre, tvRate, tvDescription, tvOverview;
    private View line;
    private DetailTvShowPresenter presenterDetail;
    private FavoriteView.TvShow favoriteTvShow;
    private String id;
    private SwipeRefreshLayout swipeRefreshLayout;
    StringBuilder genres;
    public static final String EXTRA_TV_SHOW = "Catalog";

    static String STATE_TITLE = "Title";
    static String STATE_DATE = "Date";
    static String STATE_GENRE = "Genre";
    static String STATE_RATE = "Rate";
    static String STATE_POSTER = "Poster";
    static String STATE_OVERVIEW = "Overview";

    private String tvshowTitle;
    private String tvshowDate;
    private String tvshowGenre;
    private String tvshowRate;
    private String tvshowPoster;
    private String tvshowOverview;

    private Menu menu;
    private TvShowEntity tvshowEntity = new TvShowEntity();
    private AppDatabase tvshowDatabase;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tvshow);
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

        tvshowDatabase = AppDatabase.Companion.getInstance(this);
        favoriteTvShow = new FavoriteTvShowPresenter(tvshowDatabase.tvshowDao());

        id = getIntent().getStringExtra(EXTRA_TV_SHOW);
        presenterDetail = new DetailTvShowPresenter(this);

        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        if (savedInstanceState == null) {
            loadData();
        }

        favoriteState();
    }

    @Override
    public void showTvShows(@NotNull TvShow tvshow) {
        try {
            tvshowTitle = tvshow.getTitle();
            tvshowDate = tvshow.getDate();
            tvshowRate = tvshow.getRate();
            tvshowPoster = tvshow.getPoster();
            tvshowOverview = tvshow.getDescription();

            tvTitle.setText(tvshowTitle);
            tvDate.setText(tvshowDate);
            tvRate.setText(tvshowRate);
            tvDescription.setText(tvshowOverview);
            Glide.with(this)
                    .load(BuildConfig.POSTER_URL + tvshowPoster)
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
                presenterDetail.getTvShowDetail(BuildConfig.API_KEY, language, id);
                break;
            case "in":
                language = "id";
                presenterDetail.getTvShowDetail(BuildConfig.API_KEY, language, id);
                break;
        }
    }

    @Override
    public void showGenre(@NotNull ArrayList<GenreTvShow> genre) {
        String genreList = "";
        genres = new StringBuilder(genreList);
        for (int i = 0; i < genre.size(); i++) {
            genres.append(genre.get(i).getGenre()).append(". ");
        }
        tvGenre.setText(genres);
        tvGenre.setAnimation(AnimationUtils.loadAnimation(this, R.anim.item_fall_down));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_TITLE, tvshowTitle);
        outState.putString(STATE_DATE, tvshowDate);
        outState.putString(STATE_GENRE, tvshowGenre);
        outState.putString(STATE_RATE, tvshowRate);
        outState.putString(STATE_POSTER, tvshowPoster);
        outState.putString(STATE_OVERVIEW, tvshowOverview);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvshowTitle = savedInstanceState.getString(STATE_TITLE);
        tvshowDate = savedInstanceState.getString(STATE_DATE);
        tvshowGenre = savedInstanceState.getString(STATE_GENRE);
        tvshowRate = savedInstanceState.getString(STATE_RATE);
        tvshowPoster = savedInstanceState.getString(STATE_POSTER);
        tvshowOverview = savedInstanceState.getString(STATE_OVERVIEW);

        tvTitle.setText(tvshowTitle);
        tvDate.setText(tvshowDate);
        tvGenre.setText(tvshowGenre);
        tvRate.setText(tvshowRate);
        tvDescription.setText(tvshowOverview);
        Glide.with(this)
                .load(BuildConfig.POSTER_URL + tvshowPoster)
                .into(imgPoster);
    }

    private void addToFavorite() {
        tvshowEntity.setTvshowId(id);
        tvshowEntity.setTvshowTitle(tvshowTitle);
        tvshowEntity.setTvshowDate(tvshowDate);
        tvshowEntity.setTvshowRate(tvshowRate);
        tvshowEntity.setTvshowPoster(tvshowPoster);

        favoriteTvShow.addTvShow(tvshowEntity);

        Toast.makeText(this, R.string.added_favorite, Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorite() {
        favoriteTvShow.deleteTvShow(id);

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
        TvShowEntity[] favorite = tvshowDatabase.tvshowDao().getTvShowById(id);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenterDetail.onDestroy();
    }
}
