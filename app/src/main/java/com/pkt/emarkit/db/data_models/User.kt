package com.pkt.emarkit.db.data_models

import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
@Entity(tableName = "user")
class User: Serializable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("address")
    @Expose
    var address: String = ""

    @SerializedName("city")
    @Expose
    var city: String = ""

    @SerializedName("created_at")
    @Expose
    var created_at: String =""

    @SerializedName("created_by")
    @Expose
    var created_by: Int? = null

    @SerializedName("district")
    @Expose
    var district: String? = null

    @SerializedName("email")
    @Expose
    var email: String = ""

    @SerializedName("email_verified_at")
    @Expose
    var email_verified_at: String? = null

    @SerializedName("is_active")
    @Expose
    var is_active: Int = 0

    @SerializedName("lat")
    @Expose
    var lat: String =""

    @SerializedName("lng")
    @Expose
    var lng: String =""

    @SerializedName("mobile")
    @Expose
    var mobile: String =""

    @SerializedName("modified_by")
    @Expose
    var modified_by: Int? = null

    @SerializedName("name")
    @Expose
    var name: String =""

    @SerializedName("org_type")
    @Expose
    var org_type: String =""

    @SerializedName("organisation")
    @Expose
    var organisation: String = ""

    @SerializedName("pincode")
    @Expose
    var pincode: Int? = null

    @SerializedName("profile_image")
    @Expose
    var profile_image: String? = null

    @SerializedName("state")
    @Expose
    var state: String = ""

    @SerializedName("status")
    @Expose
    var status: Int = 0

    @SerializedName("updated_at")
    @Expose
    var updated_at: String = ""
}