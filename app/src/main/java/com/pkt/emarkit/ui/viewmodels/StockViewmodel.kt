package com.pkt.emarkit.ui.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.pkt.emarkit.api.RetrofitClientObj
import com.pkt.emarkit.api.api_models.BalanceStock
import com.pkt.emarkit.api.api_models.BalanceStockX
import com.pkt.emarkit.api.api_models.StockSummary
import com.pkt.emarkit.api.api_models.StockSummaryRequest
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewBinders.ItemWiseProductBinder
import com.pkt.emarkit.ui.viewBinders.PartyWiseProductBinder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockViewmodel(application: Application):BaseViewmodel(application) {

    var stockType:MutableLiveData<String> = MutableLiveData("")
    var typeWise:MutableLiveData<String> = MutableLiveData("")
    var search_value:MutableLiveData<String> = MutableLiveData("")
    var isLoading = MutableLiveData(false)


    var partyWiseProductBinder:MutableLiveData<List<PartyWiseProductBinder>> = MutableLiveData<List<PartyWiseProductBinder>>()
    var itemWiseProductBinder:MutableLiveData<List<ItemWiseProductBinder>> = MutableLiveData<List<ItemWiseProductBinder>>()

    fun stockSummary(callback: RetrofitCallback){
        var stockSummaryRequest = StockSummaryRequest()
        stockSummaryRequest.search = search_value.value
        stockSummaryRequest.status = stockType.value
        isLoading.value = true
        RetrofitClientObj(context).instance.stockSummary(stockSummaryRequest).enqueue(object : Callback<StockSummary> {
            override fun onResponse(call: Call<StockSummary>, response: Response<StockSummary>) {
                if (response.isSuccessful){
                    isLoading.value = false


                    partyWiseProductBinder.postValue(convertModel(response.body()!!.party_wise!!.balance_stock))
                    itemWiseProductBinder.postValue(convertModel1(response.body()!!.item_wise!!.balance_stock))
                }
                else{
                    isLoading.value = false
                    callback.onFail("Response Unsuccessful")
                }
            }
            override fun onFailure(call: Call<StockSummary>, t: Throwable) {
                isLoading.value = false
                callback.onApiFail(t)
            }

        })
    }

    private fun convertModel1(balanceStock: List<BalanceStock>?): ArrayList<ItemWiseProductBinder>? {

        var itemWiseProductBinder:ArrayList<ItemWiseProductBinder> = ArrayList<ItemWiseProductBinder>()

        for (model in balanceStock!!){
            var IWPB = ItemWiseProductBinder()
            IWPB.product_name = model.name
            IWPB.image = model.image
            IWPB.uom = model.uom
            IWPB.weight = model.base_weight
            IWPB.qty = model.qty.toString()

            itemWiseProductBinder.add(IWPB)
        }
        return itemWiseProductBinder
    }

    private fun convertModel(balanceStock: List<BalanceStockX>?): ArrayList<PartyWiseProductBinder>? {
         var partyWiseProductBinder:ArrayList<PartyWiseProductBinder> = ArrayList<PartyWiseProductBinder>()

        for (model in balanceStock!!){
            var PWPB = PartyWiseProductBinder()
            PWPB.name = model.name
            PWPB.quantity = model.value.toString()

            partyWiseProductBinder.add(PWPB)
        }
        return partyWiseProductBinder
    }

}