package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class Detail {
    @SerializedName("order_detail_code")
    @Expose
    val order_detail_code: String =""

    @SerializedName("price")
    @Expose
    val price: Int?=null

    @SerializedName("product_details")
    @Expose
    val product_details: ProductDetails?=null

    @SerializedName("qty")
    @Expose
    val qty: Int?=null

    @SerializedName("return_qty")
    @Expose
    var return_qty: String=""
}