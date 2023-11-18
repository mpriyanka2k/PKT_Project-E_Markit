package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class StockSummary {

    @SerializedName("item_wise")
    @Expose
    val item_wise: ItemWise? = null

    @SerializedName("party_wise")
    @Expose
    val party_wise: PartyWise? = null

    @SerializedName("stock_summary")
    @Expose
    val stock_summary: StockSummaryX? = null
}