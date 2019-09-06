package com.example.mymoviecatalogue.view

import com.example.mymoviecatalogue.model.GenreMovie
import com.example.mymoviecatalogue.model.Movie

class DetailMovieView {
    interface View {
        fun showMovies(movie:  Movie)
        fun showGenre(genre: ArrayList<GenreMovie>)
        fun showProgressBar()
        fun hideProgressBar()
        fun errorHandler(e: Throwable)
    }

    interface Presenter {
        fun getMovieDetail(api: String, language: String, movieid : String)
        fun onDestroy()
    }
}