package com.example.mymoviecatalogue.view

import com.example.mymoviecatalogue.model.GenreTvShow
import com.example.mymoviecatalogue.model.TvShow

class DetailTvShowView {
    interface View {
        fun showTvShows(tvshow: TvShow)
        fun showGenre(genre: ArrayList<GenreTvShow>)
        fun showProgressBar()
        fun hideProgressBar()
        fun errorHandler(e: Throwable)
    }

    interface Presenter {
        fun getTvShowDetail(api: String, language: String, tvid: String)
        fun onDestroy()
    }
}