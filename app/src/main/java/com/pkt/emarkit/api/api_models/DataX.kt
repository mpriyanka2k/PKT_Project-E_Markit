package com.pkt.emarkit.api.api_models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Keep
class DataX {

    @SerializedName("id")
    @Expose
    val id: Int? =0

    @SerializedName("created_at")
    @Expose
    val created_at: String? = ""

    @SerializedName("created_by")
    @Expose
    val created_by: Int? = null

    @SerializedName("delivery_code")
    @Expose
    val delivery_code: String? =""

    @SerializedName("delivery_date")
    @Expose
    val delivery_date: String? =""

    @SerializedName("delivery_status")
    @Expose
    val delivery_status: String?=""

    @SerializedName("status")
    @Expose
    val status: Int? = null

    @SerializedName("details")
    @Expose
    val details: List<Detail>? = null

    @SerializedName("drop_point_details")
    @Expose
    val drop_point_details: DropPointDetails? = null

    @SerializedName("invoice")
    @Expose
    val invoice: String? =""

    @SerializedName("invoice_no")
    @Expose
    val invoice_no: String? =""

    @SerializedName("is_active")
    @Expose
    val is_active: Int? =null

    @SerializedName("modified_at")
    @Expose
    val modified_at: String? =""

    @SerializedName("modified_by")
    @Expose
    val modified_by: String?=""

    @SerializedName("order_code")
    @Expose
    val order_code: String? =""

    @SerializedName("order_date")
    @Expose
    val order_date: String?=""

    @SerializedName("pending_amt")
    @Expose
    val pending_amt: String? =""

    @SerializedName("received_amt")
    @Expose
    val received_amt: String? = ""

    @SerializedName("sr_no")
    @Expose
    val sr_no: Int? =null

    @SerializedName("total_amt")
    @Expose
    val total_amt: Int? = null

    @SerializedName("user_details")
    @Expose
    val user_details: String? = ""

//    constructor(parcel: Parcel) : this() {
//        parcel.readInt()
//        parcel.readString()
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeInt(id!!)
//        parcel.writeString(created_at)
//        parcel.writeString(created_by?.toString())
//        parcel.writeString(delivery_code)
//        parcel.writeString(delivery_date)
//        parcel.writeString(delivery_status)
//        if (status != null) {
//            parcel.writeInt(status)
//        }
//        parcel
////        parcel.writeString(name)
////        parcel.writeInt(age)
//    }
//
//    companion object CREATOR : Parcelable.Creator<DataX> {
//        override fun createFromParcel(parcel: Parcel): DataX {
//            return DataX(parcel)
//        }
//
//        override fun newArray(size: Int): Array<DataX?> {
//            return arrayOfNulls(size)
//        }
//    }

}