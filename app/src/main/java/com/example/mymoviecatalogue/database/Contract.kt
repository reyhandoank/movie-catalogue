package com.example.mymoviecatalogue.database

import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

object Contract {

    const val TABLE_NAME = "favorite_movie"
    const val AUTHORITY = "com.example.mymoviecatalogue"
    private const val SCHEME = "content"

    class Columns : BaseColumns {
        companion object {
            const val ID = "id"
            const val MOVIE_ID = "movie_id"
            const val MOVIE_TITLE = "title"
            const val MOVIE_DATE = "date"
            const val MOVIE_RATE = "rate"
            const val MOVIE_POSTER = "image_poster"

            val CONTENT_URI: Uri = Uri.Builder()
                    .scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }

    }

    fun getColumnString(cursor: Cursor, columnName: String): String {
        return cursor.getString(cursor.getColumnIndex(columnName))
    }

    fun getColumnInt(cursor: Cursor, columnName: String): Int {
        return cursor.getInt(cursor.getColumnIndex(columnName))
    }
}