package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class LoginRequest {
    @SerializedName("email")
    @Expose
    var email: String = ""

    @SerializedName("password")
    @Expose
    var password: String =""

    @SerializedName("notEncrypted")
    @Expose
    var notEncrypted: Boolean =false
}