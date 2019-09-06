package com.example.mymoviecatalogue.presenter

import androidx.lifecycle.ViewModel
import com.example.mymoviecatalogue.database.MovieDao
import com.example.mymoviecatalogue.database.MovieEntity
import com.example.mymoviecatalogue.view.FavoriteView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoriteMoviePresenter(private val movieDao: MovieDao) : ViewModel(), FavoriteView.Movie {
    private val compositeDisposable = CompositeDisposable()

    override fun addMovie(movie: MovieEntity) {
        compositeDisposable.add(Observable.fromCallable { movieDao.addMovie(movie) }
                .subscribeOn(Schedulers.computation()).subscribe()
        )
    }

    override fun deleteMovie(movieId: String) {
        compositeDisposable.add(Observable.fromCallable { movieDao.deleteMovie(movieId) }
                .subscribeOn(Schedulers.computation()).subscribe()
        )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

}