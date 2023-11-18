package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class confirmDeliveryResponse {

    @SerializedName("status")
    @Expose
    var status:String? = ""

    @SerializedName("message")
    @Expose
    var message:String? = ""
}