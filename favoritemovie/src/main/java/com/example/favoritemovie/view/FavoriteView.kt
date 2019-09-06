package com.example.favoritemovie.view

import android.content.Context
import com.example.favoritemovie.model.Movie

class FavoriteView {
    interface View {
        fun showFavoriteMovie(movie: List<Movie>)
        fun errorHandler(throwable: String?)
    }

    interface Presenter {
        fun fetchFavoriteMovie(context: Context)
        fun onDestroy()
    }
}