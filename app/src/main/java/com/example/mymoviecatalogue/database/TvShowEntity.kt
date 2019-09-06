package com.example.mymoviecatalogue.database

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tTvShow")
data class TvShowEntity(
        @NonNull
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int? = null,

        @ColumnInfo(name = "tvshowId")
        var tvshowId: String? = null,

        @ColumnInfo(name = "tvshow_title")
        var tvshowTitle: String? = null,

        @ColumnInfo(name = "tvshow_date")
        var tvshowDate: String? = null,

        @ColumnInfo(name = "tvshow_genre")
        var tvshowGenre: String? = null,

        @ColumnInfo(name = "tvshow_rate")
        var tvshowRate: String? = null,

        @ColumnInfo(name = "tvshow_poster")
        var tvshowPoster: String? = null,

        @ColumnInfo(name = "tvshow_overview")
        var tvshowOverview: String? = null
) : Parcelable