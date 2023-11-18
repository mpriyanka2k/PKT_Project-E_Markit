package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class PendingDeliveryRequest{

    @SerializedName("search")
    @Expose
    var search:SearchData? = null

    @SerializedName("length")
    @Expose
    var length:String = ""

    @SerializedName("status")
    @Expose
    var status:String =""

    @SerializedName("latitude")
    @Expose
    var latitude:Double? = null

    @SerializedName("longitude")
    @Expose
    var longitude:Double? =null

    @SerializedName("location_name")
    @Expose
    var location_name:String = ""

}