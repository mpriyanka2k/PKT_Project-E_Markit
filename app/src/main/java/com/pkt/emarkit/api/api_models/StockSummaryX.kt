package com.pkt.emarkit.api.api_models

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class StockSummaryX{

    @SerializedName("balance_stock")
    @Expose
    val balance_stock: Int = 0

    @SerializedName("return_stock")
    @Expose
    val return_stock: Int = 0

    @SerializedName("total_stock")
    @Expose
    val total_stock: Int = 0
}

