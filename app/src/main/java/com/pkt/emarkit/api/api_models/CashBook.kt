package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Keep
class CashBook {
    @SerializedName("amt")
    @Expose
    val amt: Int =0

    @SerializedName("cash_book_id")
    @Expose
    val cash_book_id: Int= 0

    @SerializedName("created_at")
    @Expose
    val created_at: String =""

    @SerializedName("id")
    @Expose
    val id: Int =0

    @SerializedName("modified_at")
    @Expose
    val modified_at: String =""

    @SerializedName("name")
    @Expose
    val name: String =""

    @SerializedName("order_code")
    @Expose
    val order_code: String =""

    @SerializedName("payment_mode")
    @Expose
    val payment_mode: String =""

    @SerializedName("type")
    @Expose
    val type: String = ""
}