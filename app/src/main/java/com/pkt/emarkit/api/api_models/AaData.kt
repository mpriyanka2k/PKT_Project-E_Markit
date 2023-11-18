package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class AaData {

    @SerializedName("cash_book_list")
    @Expose
    val cash_book_list: List<CashBook>? = null

    @SerializedName("closing_balance")
    @Expose
    val closing_balance: Int = 0

    @SerializedName("date")
    @Expose
    val date: String = ""

    @SerializedName("opening_balance")
    @Expose
    val opening_balance: Int = 0

    @SerializedName("sr_no")
    @Expose
    val sr_no: Int = 0
}