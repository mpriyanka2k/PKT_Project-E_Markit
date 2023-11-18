package com.pkt.emarkit.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pkt.emarkit.api.RetrofitClientObj
import com.pkt.emarkit.api.api_models.DashboardResponse
import com.pkt.emarkit.api.api_models.DataXX
import com.pkt.emarkit.api.api_models.StockSummary
import com.pkt.emarkit.api.api_models.StockSummaryRequest
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewBinders.DashboardBinder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewmodel(application: Application):BaseViewmodel(application) {

    var dashboardBinder = DashboardBinder()
    var loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()


    fun getDashboardData(){
        loading.value = true
//        dashboardBinder.loading.value = true
        RetrofitClientObj(context).instance.getDashboard().enqueue(object :Callback<DashboardResponse>{
            override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                loading.value = false
//                dashboardBinder.loading.value = false
              if (response.isSuccessful){
                 if (response.body()?.status.equals("success")){
                   Log.e("priyanka","response = ${response.body()!!.data}")
                     var dataxx = ArrayList<DataXX>()
                     dataxx.addAll(response.body()!!.data!!)

                     for (data in dataxx){
                         if (data.delivery_status.equals("Pending Deliveries")){
                             dashboardBinder.pending_count.value = data.count.toString()
                         }
                         if (data.delivery_status.equals("Cancelled Deliveries")){
                             dashboardBinder.cancelled_count.value = data.count.toString()
                         }
                         if (data.delivery_status.equals("Completed Deliveries")){
                             dashboardBinder.completed_count.value = data.count.toString()
                         }
                     }
                 }
                  else{
                     showToast(response.message())
                 }
              }

                else{
                    loading.value = false
//                  dashboardBinder.loading.value= false
              }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                loading.value = false
//                dashboardBinder.loading.value = false

            }

        })
    }

    fun stockData(callback:RetrofitCallback){
        loading.value = true
        var stockSummaryRequest = StockSummaryRequest()
        stockSummaryRequest.search = ""
        stockSummaryRequest.status = ""
        RetrofitClientObj(context).instance.stockSummary(stockSummaryRequest).enqueue(object :Callback<StockSummary>{
            override fun onResponse(call: Call<StockSummary>, response: Response<StockSummary>) {
                loading.value= false
                if(response.isSuccessful){
                    if (response.body() != null){
                        dashboardBinder.stock_balance.value = response.body()!!.stock_summary!!.balance_stock.toString()
                        dashboardBinder.stock_returned.value = response.body()!!.stock_summary!!.return_stock.toString()
                        dashboardBinder.stock_ledger.value = response.body()!!.stock_summary!!.total_stock.toString()
                    }
                    else{
                        showToast(response.message())
                    }

                }
                else{
                    callback.onFail("Response Unsuccessful")
                }

            }

            override fun onFailure(call: Call<StockSummary>, t: Throwable) {
                loading.value= false
                callback.onApiFail(t)
            }

        })

    }
}