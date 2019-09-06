package com.example.mymoviecatalogue.presenter

import androidx.lifecycle.ViewModel
import com.example.mymoviecatalogue.database.TvShowDao
import com.example.mymoviecatalogue.database.TvShowEntity
import com.example.mymoviecatalogue.view.FavoriteView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoriteTvShowPresenter(private val tvShowDao: TvShowDao) : ViewModel(), FavoriteView.TvShow {
    private val compositeDisposable = CompositeDisposable()

    override fun addTvShow(tvshow: TvShowEntity) {
        compositeDisposable.add(Observable.fromCallable { tvShowDao.addTvShow(tvshow) }
                .subscribeOn(Schedulers.computation()).subscribe()
        )
    }

    override fun deleteTvShow(tvshowId: String) {
        compositeDisposable.add(Observable.fromCallable { tvShowDao.deleteTvShow(tvshowId) }
                .subscribeOn(Schedulers.computation()).subscribe()
        )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

}