package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class DropPointDetails {

    @SerializedName("address")
    @Expose
    val address: String=""

    @SerializedName("city")
    @Expose
    val city: String=""

    @SerializedName("contact_no")
    @Expose
    val contact_no: String=""

    @SerializedName("district")
    @Expose
    val district: String=""

    @SerializedName("email")
    @Expose
    val email: String=""

    @SerializedName("lat")
    @Expose
    val lat: String=""

    @SerializedName("lng")
    @Expose
    val lng: String=""

    @SerializedName("name")
    @Expose
    val name: String=""

    @SerializedName("pincode")
    @Expose
    val pincode: String=""

    @SerializedName("state")
    @Expose
    val state: String=""
}