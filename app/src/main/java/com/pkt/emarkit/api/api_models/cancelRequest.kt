package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Keep
class cancelRequest {

    @SerializedName("order_code")
    @Expose
    var order_code:String? = null

    @SerializedName("status")
    @Expose
    var status:String? = null

    @SerializedName("remark")
    @Expose
    var remark:String? = null


}