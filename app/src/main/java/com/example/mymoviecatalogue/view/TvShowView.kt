package com.example.mymoviecatalogue.view

class TvShowView {
    interface View {
        fun showProgressBar()
        fun hideProgressBar()
        fun errorHandler(e: Throwable)
    }

    interface Presenter {
        fun setTvShowList(api: String, page: Int, language: String, view: View)
        fun onDestroy()
    }
}