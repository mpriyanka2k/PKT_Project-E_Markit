package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class AreaListResponse {

    @SerializedName("arealist")
    @Expose
    val arealist: List<String>? = null

    @SerializedName("status")
    @Expose
    val status: String = ""
}