package com.pkt.emarkit.ui.viewBinders

import android.os.Parcel
import android.os.Parcelable

class ProductItemParceble():Parcelable {

    var id = ""
    var product_name:String? = ""
    var product_item:String? = ""
    var amount:String? = ""
    //    var total_qty:String? = "0"
    var total_qty:String = "0"
    var total_qty1:Int? = 0
    var product_img:String = ""
    var available_qty = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        product_name = parcel.readString()
        product_item = parcel.readString()
        amount = parcel.readString()
        total_qty = parcel.readString().toString()
        total_qty1 = parcel.readValue(Int::class.java.classLoader) as? Int
        product_img = parcel.readString().toString()
        available_qty = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(product_name)
        parcel.writeString(product_item)
        parcel.writeString(amount)
        parcel.writeString(total_qty)
        parcel.writeValue(total_qty1)
        parcel.writeString(product_img)
        parcel.writeInt(available_qty)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductItemParceble> {
        override fun createFromParcel(parcel: Parcel): ProductItemParceble {
            return ProductItemParceble(parcel)
        }

        override fun newArray(size: Int): Array<ProductItemParceble?> {
            return arrayOfNulls(size)
        }
    }
}