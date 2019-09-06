package com.example.mymoviecatalogue.view

import com.example.mymoviecatalogue.model.MovieResponse
import com.example.mymoviecatalogue.model.TvShowResponse

class SearchItemView {
    interface View {
        fun getMovieData(movies: MovieResponse)
        fun getTvShowData(tvshows: TvShowResponse)
        fun showProgressBar()
        fun hideProgressBar()
        fun errorHandler(t: Throwable?)
    }

    interface ViewModel {
        fun searchMovie(api: String, query: String?, page: Int, language: String, view: View)
        fun searchTvShow(api: String, query: String?, page: Int, language: String, view: View)
        fun onDestroy()
    }
}