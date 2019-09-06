package com.example.mymoviecatalogue.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymoviecatalogue.BuildConfig
import com.example.mymoviecatalogue.model.TvShow
import com.example.mymoviecatalogue.model.TvShowResponse
import com.example.mymoviecatalogue.view.TvShowView
import com.rx2androidnetworking.Rx2AndroidNetworking

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TvShowViewModel : ViewModel(), TvShowView.Presenter {
    private val tvShowList = MutableLiveData<ArrayList<TvShow>>()
    private val compositeDisposable = CompositeDisposable()

    fun getTvShowList() : LiveData<ArrayList<TvShow>> {
        return tvShowList
    }

    override fun setTvShowList(api: String, page: Int, language: String, view:TvShowView.View) {
        view.showProgressBar()
        Rx2AndroidNetworking.get(BuildConfig.BASE_URL + "discover/tv")
                .addQueryParameter("api_key", api)
                .addQueryParameter("page", page.toString())
                .addQueryParameter("language", language)
                .build()
                .getObjectObservable(TvShowResponse::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<TvShowResponse> {
                    override fun onComplete() {
                        view.hideProgressBar()
                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(tvShowResponse: TvShowResponse) {
                        tvShowList.postValue(tvShowResponse.results)
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
