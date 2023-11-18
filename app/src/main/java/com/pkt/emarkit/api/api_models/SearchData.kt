package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class SearchData {

    @SerializedName("value")
    @Expose
    var value:String = ""

}