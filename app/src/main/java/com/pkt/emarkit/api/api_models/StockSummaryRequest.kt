package com.pkt.emarkit.api.api_models

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class StockSummaryRequest {

    @SerializedName("status")
    @Expose
    var status: String? = ""

    @SerializedName("search")
    @Expose
    var search: String? = ""
}