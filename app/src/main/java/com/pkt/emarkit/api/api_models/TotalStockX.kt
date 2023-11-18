package com.pkt.emarkit.api.api_models

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class TotalStockX{
    @SerializedName("name")
    @Expose
    val name: String = ""

    @SerializedName("value")
    @Expose
    val value: Int = 0
}