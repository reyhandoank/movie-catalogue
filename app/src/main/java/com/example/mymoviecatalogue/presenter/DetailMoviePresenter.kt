package com.example.mymoviecatalogue.presenter

import com.example.mymoviecatalogue.BuildConfig
import com.example.mymoviecatalogue.model.Movie
import com.example.mymoviecatalogue.view.DetailMovieView
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailMoviePresenter(private val view: DetailMovieView.View) : DetailMovieView.Presenter {
    private val compositeDisposable = CompositeDisposable()

    override fun getMovieDetail(api: String, language: String, movieid: String) {
        view.showProgressBar()
        Rx2AndroidNetworking.get(BuildConfig.BASE_URL + "movie/{movie_id}")
                .addQueryParameter("api_key", api)
                .addPathParameter("movie_id", movieid)
                .addQueryParameter("language", language)
                .build()
                .getObjectObservable(Movie::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Movie> {
                    override fun onComplete() {
                        view.hideProgressBar()
                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(t: Movie) {
                        view.showMovies(t)
                        t.genre?.let { view.showGenre(it) }
                    }

                    override fun onError(e: Throwable) {
                        view.errorHandler(e)
                        view.hideProgressBar()
                    }

                })
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}
