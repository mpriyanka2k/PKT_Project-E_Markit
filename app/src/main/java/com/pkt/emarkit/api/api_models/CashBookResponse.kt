package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Keep
class CashBookResponse {

    @SerializedName("aaData")
    @Expose
    val aaData: List<AaData>? = null

    @SerializedName("closing_balance")
    @Expose
    val closing_balance: Int = 0

    @SerializedName("draw")
    @Expose
    val draw: Int = 0

    @SerializedName("iTotalDisplayRecords")
    @Expose
    val iTotalDisplayRecords: Int = 0

    @SerializedName("iTotalRecords")
    @Expose
    val iTotalRecords: Int = 0

    @SerializedName("opening_balance")
    @Expose
    val opening_balance: Int = 0

    @SerializedName("status")
    @Expose
    val status: String = ""
}