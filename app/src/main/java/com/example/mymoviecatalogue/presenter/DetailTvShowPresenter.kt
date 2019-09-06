package com.example.mymoviecatalogue.presenter

import com.example.mymoviecatalogue.BuildConfig
import com.example.mymoviecatalogue.model.TvShow
import com.example.mymoviecatalogue.view.DetailTvShowView
import com.rx2androidnetworking.Rx2AndroidNetworking

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailTvShowPresenter(private val view: DetailTvShowView.View) : DetailTvShowView.Presenter {
    private val compositeDisposable = CompositeDisposable()

    override fun getTvShowDetail(api: String, language: String, tvid: String) {
        view.showProgressBar()
        Rx2AndroidNetworking.get(BuildConfig.BASE_URL + "tv/{tv_id}")
                .addQueryParameter("api_key", api)
                .addPathParameter("tv_id", tvid)
                .addQueryParameter("language", language)
                .build()
                .getObjectObservable(TvShow::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<TvShow> {
                    override fun onComplete() {
                        view.hideProgressBar()
                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(t: TvShow) {
                        view.showTvShows(t)
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
