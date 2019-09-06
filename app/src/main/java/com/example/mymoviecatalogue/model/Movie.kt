package com.example.mymoviecatalogue.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
        @SerializedName("id")
        @Expose
        var id: Int? = null,

        @SerializedName("title")
        @Expose
        var title: String? = null,

        @SerializedName("release_date")
        @Expose
        var date: String? = null,

        @SerializedName("genres")
        @Expose
        var genre: ArrayList<GenreMovie>? = null,

        @SerializedName("vote_average")
        @Expose
        var rate: String? = null,

        @SerializedName("poster_path")
        @Expose
        var poster: String? = null,

        @SerializedName("overview")
        @Expose
        var description: String? = null
) : Parcelable
