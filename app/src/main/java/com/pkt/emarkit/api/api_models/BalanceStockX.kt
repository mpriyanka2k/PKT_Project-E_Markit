package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class BalanceStockX{

    @SerializedName("name")
    @Expose
    val name: String = ""

    @SerializedName("value")
    @Expose
    val value: Int = 0
}