package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Keep
class Data {
    @SerializedName("user_details")
    @Expose
    var user_details: UserDetails? = null

    @SerializedName("user_roles")
    @Expose
    var user_roles: List<UserRole>? = null
}