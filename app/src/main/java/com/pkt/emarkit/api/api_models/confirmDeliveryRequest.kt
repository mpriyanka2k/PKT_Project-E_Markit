package com.pkt.emarkit.api.api_models

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class confirmDeliveryRequest {

    @SerializedName("order_code")
    @Expose
    var order_code:String? = null

    @SerializedName("total_amt")
    @Expose
    var total_amt:Int? = null

    @SerializedName("received_amt")
    @Expose
    var received_amt:String? = null

    @SerializedName("pending_amt")
    @Expose
    var pending_amt:String? = null

    @SerializedName("status")
    @Expose
    var status:Int? = 0

    @SerializedName("payment_mode")
    @Expose
    var payment_mode: PaymentMode? = null

    @SerializedName("invoice")
    @Expose
    var invoice: String? = ""

    @SerializedName("invoice_no")
    @Expose
    var invoice_no: String? = ""

    @SerializedName("user_details")
    @Expose
    var user_details: String? = ""

    @SerializedName("order_date")
    @Expose
    var order_date: String? = ""

    @SerializedName("delivery_date")
    @Expose
    var delivery_date: String? = ""

    @SerializedName("is_active")
    @Expose
    var is_active: Int? = 0

    @SerializedName("created_at")
    @Expose
    var created_at: String? = ""

    @SerializedName("modified_at")
    @Expose
    var modified_at: String? = ""

    @SerializedName("created_by")
    @Expose
    var created_by: Int? = 0

    @SerializedName("modified_by")
    @Expose
    var modified_by: String? = ""

    @SerializedName("delivery_code")
    @Expose
    var delivery_code: String? = ""

    @SerializedName("delivery_status")
    @Expose
    var delivery_status: String? = ""

    @SerializedName("sr_no")
    @Expose
    var sr_no: Int? = 0

    @SerializedName("drop_point_details")
    @Expose
    var drop_point_details: DropPointDetails? = null

    @SerializedName("details")
    @Expose
    var details: Array<Detail>? = null

}