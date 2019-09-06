package com.example.mymoviecatalogue.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymoviecatalogue.BuildConfig
import com.example.mymoviecatalogue.model.Movie
import com.example.mymoviecatalogue.model.MovieResponse
import com.example.mymoviecatalogue.view.MovieView
import com.rx2androidnetworking.Rx2AndroidNetworking

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MovieViewModel : ViewModel(), MovieView.Presenter {
    private val movieList = MutableLiveData<ArrayList<Movie>>()
    private val compositeDisposable = CompositeDisposable()

    fun getMovieList() : LiveData<ArrayList<Movie>> {
        return movieList
    }

    override fun setMovieList(api: String, page: Int, language: String, view: MovieView.View) {
        view.showProgressBar()
        Rx2AndroidNetworking.get(BuildConfig.BASE_URL + "discover/movie")
                .addQueryParameter("api_key", api)
                .addQueryParameter("page", page.toString())
                .addQueryParameter("language", language)
                .build()
                .getObjectObservable(MovieResponse::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MovieResponse> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(movieResponse: MovieResponse) {
                        movieList.postValue(movieResponse.results)
                    }

                    override fun onError(e: Throwable) {
                        view.errorHandler(e)
                        view.hideProgressBar()
                    }

                    override fun onComplete() {
                        view.hideProgressBar()
                    }
                })
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}
