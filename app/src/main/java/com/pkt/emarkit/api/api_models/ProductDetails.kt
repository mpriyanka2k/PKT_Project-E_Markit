package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class ProductDetails {

    @SerializedName("image")
    @Expose
    val image: String=""

    @SerializedName("name")
    @Expose
    val name: String=""

    @SerializedName("price")
    @Expose
    val price: String=""

    @SerializedName("uom")
    @Expose
    val uom: String=""
}