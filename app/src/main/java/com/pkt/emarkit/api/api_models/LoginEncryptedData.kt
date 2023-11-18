package com.pkt.emarkit.api.api_models

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class LoginEncryptedData {

    @SerializedName("ciphertext")
    @Expose
    val ciphertext: String? = null

    @SerializedName("iv")
    @Expose
    val iv: String? = null

    @SerializedName("salt")
    @Expose
    val salt: String? = null

    @SerializedName("iterations")
    @Expose
    val iterations: Int? = null
}