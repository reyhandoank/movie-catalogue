package com.example.favoritemovie.model

import android.database.Cursor
import android.os.Parcelable
import com.example.favoritemovie.database.Contract.Columns.Companion.MOVIE_DATE
import com.example.favoritemovie.database.Contract.Columns.Companion.MOVIE_ID
import com.example.favoritemovie.database.Contract.Columns.Companion.MOVIE_POSTER
import com.example.favoritemovie.database.Contract.Columns.Companion.MOVIE_RATE
import com.example.favoritemovie.database.Contract.Columns.Companion.MOVIE_TITLE
import com.example.favoritemovie.database.Contract.getColumnInt
import com.example.favoritemovie.database.Contract.getColumnString
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
        var id: Int? = null,

        var title: String? = null,

        var date: String? = null,

        var rate: String? = null,

        var poster: String? = null
) : Parcelable {
    constructor(cursor: Cursor?) : this() {
        this.id = getColumnInt(cursor, MOVIE_ID)
        this.title = getColumnString(cursor, MOVIE_TITLE)
        this.date = getColumnString(cursor, MOVIE_DATE)
        this.rate = getColumnString(cursor, MOVIE_RATE)
        this.poster = getColumnString(cursor, MOVIE_POSTER)
    }
}
