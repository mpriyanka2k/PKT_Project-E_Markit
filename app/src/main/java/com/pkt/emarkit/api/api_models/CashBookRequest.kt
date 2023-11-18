package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class CashBookRequest {

    @SerializedName("from_date")
    @Expose
    var from_date: String = ""

    @SerializedName("to_date")
    @Expose
    var to_date: String = ""

    @SerializedName("length")
    @Expose
    var length: String = ""

    @SerializedName("search")
    @Expose
    var search: Search? = null
}