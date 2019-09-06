package com.example.mymoviecatalogue.view

class MovieView {
    interface View {
        fun showProgressBar()
        fun hideProgressBar()
        fun errorHandler(e: Throwable)
    }

    interface Presenter {
        fun setMovieList(api: String, page: Int, language: String, view: View)
        fun onDestroy()
    }
}