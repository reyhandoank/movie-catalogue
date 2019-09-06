package com.example.mymoviecatalogue.presenter

import android.content.ContentValues
import android.content.Context
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.CONTENT_URI
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.MOVIE_DATE
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.MOVIE_ID
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.MOVIE_POSTER
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.MOVIE_RATE
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.MOVIE_TITLE
import com.example.mymoviecatalogue.model.Favorite
import com.example.mymoviecatalogue.view.FavoriteView
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoritePresenter : FavoriteView.Presenter {
    private val compositeDisposable = CompositeDisposable()

    companion object {
        fun setFavoriteContentValues(favorite: Favorite): ContentValues {
            val values = ContentValues()

            values.put(MOVIE_ID, favorite.id)
            values.put(MOVIE_TITLE, favorite.title.toString())
            values.put(MOVIE_DATE, favorite.date.toString())
            values.put(MOVIE_RATE, favorite.rate.toString())
            values.put(MOVIE_POSTER, favorite.poster.toString())
            return values
        }
    }
    override fun addFavorite(context: Context, favorite: Favorite) {
        compositeDisposable.add(Completable.create { e ->
            context.contentResolver.insert(CONTENT_URI, setFavoriteContentValues(favorite))
            e.onComplete()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorComplete()
                .subscribe())
    }

    override fun deleteFavorite(context: Context, favorite: Favorite) {
        val whereClause = String.format("%s = ?", MOVIE_ID)
        val args = arrayOf(favorite.id.toString())

        compositeDisposable.add(Completable.create { e ->
            context.contentResolver.delete(CONTENT_URI, whereClause, args)
            e.onComplete()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorComplete()
                .subscribe())
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}