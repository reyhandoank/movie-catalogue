package com.example.mymoviecatalogue.view

import android.content.Context
import com.example.mymoviecatalogue.model.Movie

class SettingView {

    interface View {
        fun setReminder(movie: ArrayList<Movie>)
        fun onSuccess(context: Context, appName: String?)
        fun errorHandler(t: Throwable?)
    }

    interface Presenter {
        fun setRepeatingReminder(context: Context, appName: String?)
    }
}