package com.pkt.emarkit.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.pkt.emarkit.api.RetrofitClientObj
import com.pkt.emarkit.api.api_models.*
import com.pkt.emarkit.db.AppDatabase
import com.pkt.emarkit.db.data_models.Delivery
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewBinders.DeliveryItemBinder
import com.pkt.emarkit.ui.viewBinders.SelectDeliveryBinder
import com.pkt.emarkit.utils.preferences.PrefsSessionManagement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class PendingViewmodel(application: Application):BaseViewmodel(application) {

    var sessionManagement = PrefsSessionManagement(context)
    var selectDeliveryBinder = SelectDeliveryBinder()
    var deliveryItem:MutableLiveData<List<DeliveryItemBinder>> = MutableLiveData<List<DeliveryItemBinder>>()
    var datax = ArrayList<DataX>()
    var areaList:MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    var location_name:MutableLiveData<String> = MutableLiveData("")
//    var datax:MutableLiveData<List<DataX>> = MutableLiveData<List<DataX>>()

    var db:AppDatabase = AppDatabase.getDatabase(context)

    @SuppressLint("SuspiciousIndentation")
    fun getPendingDelivery(location_name:String,latitude:Double?,longitude:Double?,Delivery_type:String, callback:RetrofitCallback){

        selectDeliveryBinder.isLoading.value =true
        Log.e("priyanka","token_in_vm=${sessionManagement.getAccessToken()}")
        var searchData = SearchData()
        searchData.value = selectDeliveryBinder.searchText.value.toString()

        var pendingDeliveryRequest = PendingDeliveryRequest()

        //check delivery type
        if(Delivery_type.equals("Pending Deliveries")){
            pendingDeliveryRequest.status = "1"
        }
        if (Delivery_type.equals("Cancelled Deliveries")){
            pendingDeliveryRequest.status = "2"
        }
        if (Delivery_type.equals("Completed Deliveries")){
            pendingDeliveryRequest.status = "3"
        }

        pendingDeliveryRequest.length = ""
        pendingDeliveryRequest.search = searchData
        pendingDeliveryRequest.latitude = latitude
        pendingDeliveryRequest.longitude = longitude
        pendingDeliveryRequest.location_name = location_name

        RetrofitClientObj(context).instance.getPendingDelivery(pendingDeliveryRequest).enqueue(object :Callback<PendingDeliveryResponse>{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<PendingDeliveryResponse>,
                response: Response<PendingDeliveryResponse>
            ) {
                if (response.isSuccessful){
                    selectDeliveryBinder.isLoading.value = false
                    if (response.body()?.status.equals("success")){
                        if (response.body()!= null){
                            Log.e("priyanka","isSuccessful")
                            Log.e("priyanka","response.body() ${response.body()!!.status}")

                            datax = response.body()!!.data as ArrayList<DataX>
//                            datax.postValue(response.body()!!.data)

                            var delivery_list  = ArrayList<Delivery>()

                            for(data in datax){
                                //Date
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                val dateTime = LocalDateTime.parse(data.delivery_date, formatter)
                                val date = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                Log.e("priyanka","dateTime = $date")

                                //Details data
                                var gsonObj = Gson()
                                var Details:String = gsonObj.toJson(data.details)
                                Log.e("priyanka","jsonStr = $Details")

                                var drop_point_details:String = gsonObj.toJson(data.drop_point_details)
                                Log.e("priyanka","jsonStr = $drop_point_details")

                            //delivery data
                                var delivery = Delivery(
                                    data.order_code!!,
                                    data.id!!,
                                    data.invoice!!,
                                    data.invoice_no!!,
                                    data.total_amt!!,
                                    data.received_amt!!,
                                    data.pending_amt!!,
                                    data.user_details!!,
                                    data.order_date!!,
                                    date,
                                    data.is_active!!,
                                    data.created_at!!,
                                    data.modified_at!!,
                                    data.created_by!!,
                                    data.modified_by!!,
                                    data.delivery_code!!,
                                    data.delivery_status!!,
                                    data.status!!,
                                    Details,
                                    data.sr_no!!,
                                    drop_point_details
                                )
                                delivery_list.add(delivery)
                       }
                            insertMultipleDelivery(delivery_list)
                            deliveryItem.postValue(convertModel(datax))
                            callback.onSuccess()
                        }
                    }
                }
                else{
                    selectDeliveryBinder.isLoading.value = false
                    callback.onFail("Response Unsuccessful")
                }
            }

            override fun onFailure(call: Call<PendingDeliveryResponse>, t: Throwable) {
                selectDeliveryBinder.isLoading.value = false
                Log.e("priyanka","onFailure === $t")
                callback.onApiFail(t)
            }

        })
    }

    fun getAreaList(callback: RetrofitCallback){
        RetrofitClientObj(context).instance.areaList().enqueue(object :Callback<AreaListResponse>{
            override fun onResponse(call: Call<AreaListResponse>, response: Response<AreaListResponse>) {

                if (response.isSuccessful){
                    if (response.body()!!.status.equals("success")){
                        if (response.body()!= null){
                            var list = response.body()!!.arealist
                            areaList.postValue(list!!)
                        }
                    }
                }
                else{
                    callback.onFail("Response Unsuccessful")
                }
            }

            override fun onFailure(call: Call<AreaListResponse>, t: Throwable) {
                    callback.onApiFail(t)
            }

        })
    }
    fun deleteAllDeliveries() {
      Thread{
       db.deliveryDao().deleteAll()
      }.start()
    }

    private fun insertMultipleDelivery(data:ArrayList<Delivery>) {
    Thread{
       for(item in data){
           //insert delivery
           insertDelivery(item)
           Log.e("priyanka","item = ${item.id}")
       }
    }.start()
    }

    private fun insertDelivery(item: Delivery) {
          Thread{
              db.deliveryDao().insertAll(item)
          }.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertModel(list: List<DataX>): ArrayList<DeliveryItemBinder>? {

        var pendingDeliveryItemBinder:ArrayList<DeliveryItemBinder> = ArrayList<DeliveryItemBinder>()

        for (model in list!!){
           var delivery = DeliveryItemBinder()
            delivery.id = model.id.toString()
            Log.e("priyanka","size of details = ${model.details!!.size}")
            delivery.product_name = model.drop_point_details!!.name
            delivery.shop_place = model.drop_point_details!!.district
            delivery.product_price = model.total_amt.toString()
            delivery.delivery_status = model.delivery_status.toString()

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val dateTime = LocalDateTime.parse(model.delivery_date, formatter)
            val output = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            Log.e("priyanka","dateTime1 = $output")

            delivery.date = output
            delivery.order_code = model.order_code

            pendingDeliveryItemBinder.add(delivery)

        }

        return pendingDeliveryItemBinder

    }

}