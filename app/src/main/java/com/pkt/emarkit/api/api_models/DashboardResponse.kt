package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class DashboardResponse {

    @SerializedName("data")
    @Expose
    val data: List<DataXX>? = null

    @SerializedName("message")
    @Expose
    val message: String = ""

    @SerializedName("status")
    @Expose
    val status: String = ""

}