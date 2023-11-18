package com.pkt.emarkit.db.data_models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "delivery")
data class Delivery (

    @NonNull
    @PrimaryKey
    var order_code:String,

    var id: Int,

    var invoice:String,

    var invoice_no:String,

    var total_amt:Int,

    var received_amt:String,

    var pending_amt:String,

    var user_details:String,

    var order_date:String,

    var delivery_date:String,

    var is_active:Int,

    var created_at:String,

    var modified_at:String,

    var created_by:Int,

    var modified_by:String,

    var delivery_code:String,

    var delivery_status:String,

    var status:Int,

    //data format list of api table Detail
    var details:String,

    var sr_no:Int,

    //data format of api table DropPointDetails
    var drop_point_details:String


) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(order_code)
        parcel.writeInt(id)
        parcel.writeString(invoice)
        parcel.writeString(invoice_no)
        parcel.writeInt(total_amt)
        parcel.writeString(received_amt)
        parcel.writeString(pending_amt)
        parcel.writeString(user_details)
        parcel.writeString(order_date)
        parcel.writeString(delivery_date)
        parcel.writeInt(is_active)
        parcel.writeString(created_at)
        parcel.writeString(modified_at)
        parcel.writeInt(created_by)
        parcel.writeString(modified_by)
        parcel.writeString(delivery_code)
        parcel.writeString(delivery_status)
        parcel.writeInt(status)
        parcel.writeString(details)
        parcel.writeInt(sr_no)
        parcel.writeString(drop_point_details)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Delivery> {
        override fun createFromParcel(parcel: Parcel): Delivery {
            return Delivery(parcel)
        }

        override fun newArray(size: Int): Array<Delivery?> {
            return arrayOfNulls(size)
        }
    }
}