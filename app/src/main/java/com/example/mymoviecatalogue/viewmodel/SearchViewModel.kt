package com.example.mymoviecatalogue.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymoviecatalogue.BuildConfig
import com.example.mymoviecatalogue.model.Movie
import com.example.mymoviecatalogue.model.MovieResponse
import com.example.mymoviecatalogue.model.TvShow
import com.example.mymoviecatalogue.model.TvShowResponse
import com.example.mymoviecatalogue.view.SearchItemView
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel : ViewModel(), SearchItemView.ViewModel {

    private val movieList = MutableLiveData<ArrayList<Movie>>()
    private val tvshowList = MutableLiveData<ArrayList<TvShow>>()
    private val compositeDisposable = CompositeDisposable()

    fun getMovieList(): LiveData<ArrayList<Movie>> {
        return movieList
    }

    fun getTvShowList(): LiveData<ArrayList<TvShow>> {
        return tvshowList
    }

    override fun searchMovie(api: String, query: String?, page: Int, language: String, view: SearchItemView.View) {
        view.showProgressBar()
        Rx2AndroidNetworking.get(BuildConfig.BASE_URL + "search/movie")
                .addQueryParameter("api_key", api)
                .addQueryParameter("query", query)
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
                        movieResponse.let { view.getMovieData(it) }
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

    override fun searchTvShow(api: String, query: String?, page: Int, language: String, view: SearchItemView.View) {
        view.showProgressBar()
        Rx2AndroidNetworking.get(BuildConfig.BASE_URL + "search/tv")
                .addQueryParameter("api_key", api)
                .addQueryParameter("query", query)
                .addQueryParameter("page", page.toString())
                .addQueryParameter("language", language)
                .build()
                .getObjectObservable(TvShowResponse::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<TvShowResponse> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(tvshowResponse: TvShowResponse) {
                        tvshowResponse.let { view.getTvShowData(it) }
                        tvshowList.postValue(tvshowResponse.results)
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