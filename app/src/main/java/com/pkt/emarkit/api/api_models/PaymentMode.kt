package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class PaymentMode {
    @SerializedName("upi")
    @Expose
    var upi:String? = ""

    @SerializedName("cash")
    @Expose
    var cash:String? = ""
}