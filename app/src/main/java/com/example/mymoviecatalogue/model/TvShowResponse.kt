package com.example.mymoviecatalogue.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class TvShowResponse (
        @SerializedName("page")
        @Expose
        var page : Int? = null,

        @SerializedName("total_results")
        @Expose
        var total_results : Int? = null,

        @SerializedName("total_pages")
        @Expose
        var total_pages : Int? = null,

        @SerializedName("results")
        @Expose
        var results : ArrayList<TvShow>? = null
) : Parcelable
