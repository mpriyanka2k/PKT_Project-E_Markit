package com.pkt.emarkit.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.pkt.emarkit.api.RetrofitClientObj
import com.pkt.emarkit.api.api_models.Detail
import com.pkt.emarkit.api.api_models.DropPointDetails
import com.pkt.emarkit.api.api_models.confirmDeliveryRequest
import com.pkt.emarkit.api.api_models.confirmDeliveryResponse
import com.pkt.emarkit.db.AppDatabase
import com.pkt.emarkit.db.data_models.Delivery
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewBinders.ProductItemParceble
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderSubmitViewmodel(application: Application):BaseViewmodel(application) {

    var Due_amount:MutableLiveData<Int> = MutableLiveData(0)
    var Cash:MutableLiveData<Int> = MutableLiveData(0)
    var UPI:MutableLiveData<Int> = MutableLiveData(0)
    var Total_amount:MutableLiveData<Int> = MutableLiveData(0)
    var received_amt:MutableLiveData<Int> = MutableLiveData(0)
    var isValid:Boolean = true

    fun confirmDelivery(delivery: Delivery, productitem: ArrayList<ProductItemParceble>, order_code:String, total_amount:Int, received_amt:String, pending_amt:String, callback: RetrofitCallback){
        isValid = true

        var details = delivery.details
        var gson = Gson()
        var detailslist = gson.fromJson(details,Array<Detail>::class.java)
        var lenght = productitem.size
        var lenght1 = lenght-1

        for (i in 0..lenght1){
           detailslist[i].return_qty = productitem[i].total_qty
        }

        Log.e("priyanka","detailslist_size = ${detailslist.size}")
        Log.e("priyanka","productitem_size = ${productitem.size}")
        if (!(Cash.value!! <= total_amount)){
            isValid = false
            callback.onFail("Cash amount must be less than or equal to total amount")
        }
        if (!(UPI.value!! <= total_amount)){
            isValid = false
            callback.onFail("UPI amount must be less than or equal to total amount")
        }

        var received_amt_int = received_amt.toInt()
        if (!(received_amt_int <= total_amount)){
            isValid = false
            callback.onFail("received amount must be less than or equal to total amount")
        }

        if (isValid){
            var confirmdeliveryRequest = confirmDeliveryRequest()

            confirmdeliveryRequest.order_code = order_code
            confirmdeliveryRequest.pending_amt = pending_amt
            confirmdeliveryRequest.received_amt = received_amt
            confirmdeliveryRequest.total_amt = total_amount
            confirmdeliveryRequest.status = 3
            confirmdeliveryRequest.payment_mode?.cash = Cash.value.toString()
            confirmdeliveryRequest.payment_mode?.upi = UPI.value.toString()

            ///////////////////////////////////////////////////////////////////////////
            confirmdeliveryRequest.invoice = delivery.invoice
            confirmdeliveryRequest.invoice_no = delivery.invoice_no
            confirmdeliveryRequest.user_details = delivery.user_details
            confirmdeliveryRequest.order_date = delivery.order_date
            confirmdeliveryRequest.delivery_date = delivery.delivery_date
            confirmdeliveryRequest.is_active = delivery.is_active
            confirmdeliveryRequest.created_at = delivery.created_at
            confirmdeliveryRequest.modified_at = delivery.modified_at
            confirmdeliveryRequest.created_by = delivery.created_by
            confirmdeliveryRequest.modified_by = delivery.modified_by
            confirmdeliveryRequest.delivery_code = delivery.delivery_code
            confirmdeliveryRequest.delivery_status = delivery.delivery_status
            confirmdeliveryRequest.sr_no = delivery.sr_no
            confirmdeliveryRequest.drop_point_details = gson.fromJson(delivery.drop_point_details,DropPointDetails::class.java)
            confirmdeliveryRequest.details = detailslist

//           var details = delivery.details
//            var gson = Gson()
//            var detailslist = gson.fromJson(details,Array<Detail>::class.java)

            ///////////////////////////////////////////////////////////////////////////

            RetrofitClientObj(context).instance.confirmDelivery(confirmdeliveryRequest).enqueue(object :Callback<confirmDeliveryResponse>{
                override fun onResponse(
                    call: Call<confirmDeliveryResponse>,
                    response: Response<confirmDeliveryResponse>
                ) {
                    if (response.isSuccessful){
                        if (response.body()!!.status.equals("success")){
                            showToast(response.message())
                            callback.onSuccess()
                        }
                        else{
                            showToast(response.message())
                        }
                    }
                    else{
                        callback.onFail("Response Unsuccessful")
                    }
                }

                override fun onFailure(call: Call<confirmDeliveryResponse>, t: Throwable) {
                    callback.onApiFail(t)
                }

            })

        }
//        ///////////////////////////////////////////////////////////////////////
//        var confirmdeliveryRequest = confirmDeliveryRequest()
//
//        confirmdeliveryRequest.order_code = order_code
//        confirmdeliveryRequest.pending_amt = pending_amt
//        confirmdeliveryRequest.received_amt = received_amt
//        confirmdeliveryRequest.total_amt = total_amount
//        confirmdeliveryRequest.status = 3
//        confirmdeliveryRequest.payment_mode!!.cash = Cash.value.toString()
//        confirmdeliveryRequest.payment_mode!!.upi = UPI.value.toString()
//
//        RetrofitClientObj(context).instance.confirmDelivery(confirmdeliveryRequest).enqueue(object :Callback<confirmDeliveryResponse>{
//            override fun onResponse(
//                call: Call<confirmDeliveryResponse>,
//                response: Response<confirmDeliveryResponse>
//            ) {
//                if (response.isSuccessful){
//                    if (response.body()!!.status.equals("success")){
//                        showToast(response.message())
//                        callback.onSuccess()
//                    }
//                }
//                else{
//                    callback.onFail("Response Unsuccessful")
//                }
//            }
//
//            override fun onFailure(call: Call<confirmDeliveryResponse>, t: Throwable) {
//                callback.onApiFail(t)
//            }
//
//        })
//
//        //////////////////////////////////////////////////////////////////////////////
    }


}