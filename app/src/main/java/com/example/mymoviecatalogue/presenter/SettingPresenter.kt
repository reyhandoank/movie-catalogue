package com.example.mymoviecatalogue.presenter

import android.content.Context
import com.example.mymoviecatalogue.BuildConfig
import com.example.mymoviecatalogue.model.MovieResponse
import com.example.mymoviecatalogue.view.SettingView
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class SettingPresenter(private val view: SettingView.View) : SettingView.Presenter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val date = Date()
    private val currentDate = dateFormat.format(date)

    private val compositeDisposable = CompositeDisposable()

    override fun setRepeatingReminder(context: Context, appName: String?) {
        Rx2AndroidNetworking.get(BuildConfig.BASE_URL + "discover/movie")
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("primary_release_date.gte", currentDate)
                .addQueryParameter("primary_release_date.lte", currentDate)
                .build()
                .getObjectObservable(MovieResponse::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MovieResponse> {
                    override fun onComplete() {
                        view.onSuccess(context, appName)
                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(t: MovieResponse) {
                        t.results?.let { view.setReminder(it) }
                    }

                    override fun onError(e: Throwable) {
                        view.errorHandler(t = Throwable())
                    }

                })
    }

}