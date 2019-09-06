package com.example.mymoviecatalogue.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GenreMovie (
        @SerializedName("name")
        @Expose
        var genre : String? = null
) : Parcelable
