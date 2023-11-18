package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class Search {
    @SerializedName("from_date")
    @Expose
    val from_date: String = ""
}