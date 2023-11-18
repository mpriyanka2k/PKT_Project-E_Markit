package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class BalanceStock {

    @SerializedName("base_weight")
    @Expose
    val base_weight: String = ""

    @SerializedName("image")
    @Expose
    val image: String = ""

    @SerializedName("name")
    @Expose
    val name: String = ""

    @SerializedName("price")
    @Expose
    val price: String = ""

    @SerializedName("qty")
    @Expose
    val qty: Int = 0

    @SerializedName("uom")
    @Expose
    val uom: String = ""
}