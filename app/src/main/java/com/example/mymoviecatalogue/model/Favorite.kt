package com.example.mymoviecatalogue.model

import android.database.Cursor
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.MOVIE_DATE
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.MOVIE_ID
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.MOVIE_POSTER
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.MOVIE_RATE
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.MOVIE_TITLE
import com.example.mymoviecatalogue.database.Contract.getColumnInt
import com.example.mymoviecatalogue.database.Contract.getColumnString

@Parcelize
data class Favorite (
        var id: Int? = null,
        var title: String? = null,
        var date: String? = null,
        var rate: String? = null,
        var poster: String? = null
) : Parcelable {
    constructor(cursor: Cursor) : this() {
        this.id = getColumnInt(cursor, MOVIE_ID)
        this.title = getColumnString(cursor, MOVIE_TITLE)
        this.date = getColumnString(cursor, MOVIE_DATE)
        this.rate = getColumnString(cursor, MOVIE_RATE)
        this.poster = getColumnString(cursor, MOVIE_POSTER)
    }
}