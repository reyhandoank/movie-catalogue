package com.example.favoritemovie.presenter

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.favoritemovie.database.Contract.Columns.Companion.CONTENT_URI
import com.example.favoritemovie.model.Movie
import com.example.favoritemovie.view.FavoriteView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoriteMoviePresenter(private val view: FavoriteView.View) : ViewModel(), FavoriteView.Presenter {
    private val compositeDisposable = CompositeDisposable()

    private fun getMovie(context: Context): Observable<List<Movie>> {
        return Observable.create { e ->
            val movie = arrayListOf<Movie>()
            val cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
            if (cursor?.moveToFirst() == true) {
                do {
                    movie.add(Movie(cursor))
                } while (cursor.moveToNext())
            }
            cursor?.close()
            e.onNext(movie)
        }
    }

    override fun fetchFavoriteMovie(context: Context) {
        compositeDisposable.add(
                getMovie(context)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ movie ->
                            view.showFavoriteMovie(movie)
                        }, {t ->
                            view.errorHandler(t.localizedMessage)
                        })
        )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }


}