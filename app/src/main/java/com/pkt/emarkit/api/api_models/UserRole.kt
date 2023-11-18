package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class UserRole {

    @SerializedName("created_at")
    @Expose
    var created_at: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("is_active")
    @Expose
    var is_active: Int? = null

    @SerializedName("modified_at")
    @Expose
    var modified_at: String? = null

    @SerializedName("role_code")
    @Expose
    var role_code: String? = null

    @SerializedName("role_name")
    @Expose
    var role_name: String? = null

    @SerializedName("slug")
    @Expose
    var slug: String? = null
}