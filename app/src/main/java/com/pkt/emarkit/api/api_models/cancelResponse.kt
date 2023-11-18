package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class cancelResponse {

    @SerializedName("status")
    @Expose
    val status: String? =""

    @SerializedName("message")
    @Expose
    val message: String? =""

}


