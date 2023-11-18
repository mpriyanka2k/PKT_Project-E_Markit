package com.pkt.emarkit.ui.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.pkt.emarkit.api.RetrofitClientObj
import com.pkt.emarkit.api.api_models.Detail
import com.pkt.emarkit.api.api_models.DropPointDetails
import com.pkt.emarkit.api.api_models.cancelRequest
import com.pkt.emarkit.api.api_models.cancelResponse

import com.pkt.emarkit.db.AppDatabase
import com.pkt.emarkit.db.data_models.Delivery
import com.pkt.emarkit.ui.adapter.ProductItemAdapter
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewBinders.DeliveryItemBinder
import com.pkt.emarkit.ui.viewBinders.ProductItemBinder
import com.pkt.emarkit.ui.viewBinders.ProductItemParceble
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DeliveryViewmodel(application: Application):BaseViewmodel(application){

    var db:AppDatabase = AppDatabase.getDatabase(context)
    var deliveryItemBinder = DeliveryItemBinder()
    var produvtItemBinder = ProductItemBinder()
    var delivery: MutableLiveData<Delivery> = MutableLiveData<Delivery>()
    lateinit var details: Array<Detail>
    var dropPointDetails = DropPointDetails()
    var detail_qty = 0
    var total_qty = 0

    var total_amount = 0
    var total_amount1 = 0
    var total_amount_with_Rs:MutableLiveData<Int> = MutableLiveData(0)

    var adapter = ProductItemAdapter()
    var total_item:MutableLiveData<Int> = MutableLiveData(0)


    var productItem:MutableLiveData<List<ProductItemBinder>> = MutableLiveData<List<ProductItemBinder>>()
    var productItemParceble:MutableLiveData<List<ProductItemParceble>> = MutableLiveData<List<ProductItemParceble>>()



    fun deliveryData(order_code:String): LiveData<Delivery> {
        return db.deliveryDao().getDelivery(order_code)
    }

    fun cancelDelivery(order_code: String,remark:String,callback: RetrofitCallback){

        var cancelRequest = cancelRequest()
        cancelRequest.remark = remark
        cancelRequest.status = "2"
        cancelRequest.order_code = order_code
        RetrofitClientObj(context).instance.cancleDelivery(cancelRequest).enqueue(object :Callback<cancelResponse>{
            override fun onResponse(
                call: Call<cancelResponse>,
                response: Response<cancelResponse>
            ) {
                if (response.isSuccessful){
                  if (response.body()!!.status.equals("success")){
                      showToast("${response.body()!!.message}")
                      callback.onSuccess()
                  }
                }
                else{
                    callback.onFail("Response Unsuccessful")
                }
            }
            override fun onFailure(call: Call<cancelResponse>, t: Throwable) {
                callback.onApiFail(t)
            }
        })

    }

    fun setData(){
        var gsonObj = Gson()
        details = gsonObj.fromJson(delivery.value!!.details,Array<Detail>::class.java)
        dropPointDetails = gsonObj.fromJson(delivery.value!!.drop_point_details,DropPointDetails::class.java)

        Log.e("priyanka","details 11= ${details}")
        Log.e("priyanka","dropPointDetails 11= $dropPointDetails")

        deliveryItemBinder.product_name = dropPointDetails.name
        deliveryItemBinder.invoice = "invoice:${delivery.value!!.invoice_no}"
        deliveryItemBinder.date = delivery.value!!.delivery_date
        deliveryItemBinder.total_amt = "Rs.${delivery.value!!.total_amt}"
        deliveryItemBinder.address = dropPointDetails.address

        total_amount = delivery.value!!.total_amt

        total_amount_with_Rs.value = delivery.value!!.total_amt

        var productItemBinder = ProductItemBinder()

        productItem.postValue(convertModel(details))

    }

    private fun convertModel(details: Array<Detail>?): List<ProductItemBinder>? {

        var productItemBinder:ArrayList<ProductItemBinder> = ArrayList<ProductItemBinder>()

        for (detail in details!!){
            var item = ProductItemBinder()
            item.product_name = detail.product_details!!.name
            item.product_item =" ${detail.qty}PC * 1${detail.product_details.uom}"
            item.amount = detail.price.toString()
            item.product_img = detail.product_details.image

//            item.total_qty = detail.qty.toString()

            item.total_qty1 = detail.qty

            item.available_qty = detail.qty!!

            detail_qty = detail.qty!!

            productItemBinder.add(item)
        }
        total_item.value = productItemBinder.size
       return productItemBinder
    }

    fun convertModelParceble(list:List<ProductItemBinder>):List<ProductItemParceble>?{
        var productItemParceble:ArrayList<ProductItemParceble> = ArrayList<ProductItemParceble>()

        for(listdata in list!!){
            var productItem = ProductItemParceble()
                productItem.id = listdata.id
                productItem.product_name = listdata.product_name
                productItem.product_item = listdata.product_item
                productItem.amount = listdata.amount
                productItem.total_qty = listdata.total_qty
                productItem.total_qty1 = listdata.total_qty1
                productItem.product_img = listdata.product_img
                productItem.available_qty = listdata.available_qty

            productItemParceble.add(productItem)

        }
        return productItemParceble
    }

    fun increase(position:Int,available_qty: Int?,single_product_amount:Int){
        total_qty = total_qty + 1

        total_amount1 = total_amount

        productItem.value!![position].total_qty = total_qty.toString()

        Log.e("priyanka","total_qty = $total_qty")
        if (total_qty > available_qty!!){
            total_qty = available_qty
            productItem.value!![position].total_qty = total_qty.toString()
            //show you can't increase th value
            Toast.makeText(context, "Total qty is $available_qty ,so you can't increase above $available_qty", Toast.LENGTH_SHORT).show()
        }

        //increase total amount
        if (total_qty <= available_qty){

            total_amount1 = total_amount1 - (single_product_amount * total_qty)
            total_amount_with_Rs.value = total_amount1


            if (total_qty == 0){
                total_amount1 = total_amount1 - single_product_amount
                total_amount_with_Rs.value = total_amount1
            }

        }

    }

    fun decrease(position: Int,available_qty: Int?,single_product_amount:Int){
        total_qty = total_qty - 1

        productItem.value!![position].total_qty = total_qty.toString()

        if (total_qty < 0){
            total_qty = 0
            productItem.value!![position].total_qty = total_qty.toString()
            showToast("Can not decrease")
        }

        //increase total amount
        if (total_qty <= available_qty!!){
            Log.e("priyanka","total_qty11 =$total_qty")
            total_amount1 = total_amount1 + (single_product_amount * total_qty)
            total_amount_with_Rs.value = total_amount1

            if (total_qty == 0){
                total_amount1 = total_amount
                total_amount_with_Rs.value = total_amount1
            }
        }


    }


}