package com.example.mymoviecatalogue.view

import android.content.Context
import com.example.mymoviecatalogue.database.MovieEntity
import com.example.mymoviecatalogue.database.TvShowEntity
import com.example.mymoviecatalogue.model.Favorite

class FavoriteView {
    interface Movie {
        fun addMovie(movie: MovieEntity)
        fun deleteMovie(movieId: String)
        fun onDestroy()
    }

    interface TvShow {
        fun addTvShow(tvshow: TvShowEntity)
        fun deleteTvShow(tvshowId: String)
        fun onDestroy()
    }

    interface Presenter {
        fun addFavorite (context: Context, favorite: Favorite)
        fun deleteFavorite(context: Context, favorite: Favorite)
        fun onDestroy()
    }
}