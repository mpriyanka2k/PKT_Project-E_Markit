package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class PartyWise {

    @SerializedName("stock")
    @Expose
    val balance_stock: List<BalanceStockX>? = null

}