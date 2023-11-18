package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class DataXX {

    @SerializedName("count")
    @Expose
    val count: Int = 0

    @SerializedName("delivery_status")
    @Expose
    val delivery_status: String = ""

    @SerializedName("status_id")
    @Expose
    val status_id: Int = 0
}