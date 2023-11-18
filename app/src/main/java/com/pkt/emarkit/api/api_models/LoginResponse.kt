package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class LoginResponse {
    @SerializedName("status")
    @Expose
    var status: String = ""

    @SerializedName("message")
    @Expose
    var message: String = ""

    @SerializedName("access_token")
    @Expose
    var access_token: String =""

    @SerializedName("data")
    @Expose
//    var data: Data? = null
    var data: String? = null

    @SerializedName("expires_in")
    @Expose
    var expires_in: Int? = null

    @SerializedName("token_type")
    @Expose
    var token_type: String = ""
}