package com.example.mymoviecatalogue.database

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tMovie")
data class MovieEntity(
        @NonNull
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int? = null,

        @ColumnInfo(name = "movieId")
        var movieId: String? = null,

        @ColumnInfo(name = "movie_title")
        var movieTitle: String? = null,

        @ColumnInfo(name = "movie_date")
        var movieDate: String? = null,

        @ColumnInfo(name = "movie_genre")
        var movieGenre: String? = null,

        @ColumnInfo(name = "movie_rate")
        var movieRate: String? = null,

        @ColumnInfo(name = "movie_poster")
        var moviePoster: String? = null,

        @ColumnInfo(name = "movie_overview")
        var movieOverview: String? = null
) : Parcelable