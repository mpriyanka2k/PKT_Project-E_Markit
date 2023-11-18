package com.pkt.emarkit.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pkt.emarkit.api.RetrofitClientObj
import com.pkt.emarkit.api.api_models.AaData
import com.pkt.emarkit.api.api_models.CashBook
import com.pkt.emarkit.api.api_models.CashBookRequest
import com.pkt.emarkit.api.api_models.CashBookResponse
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewBinders.CashBookBinder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CashBookViewmodel(application: Application):BaseViewmodel(application) {


    var currentdate: Calendar = Calendar.getInstance()
    var from_date: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance())
    var to_date: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance())
    var from_date_str: MutableLiveData<String> = MutableLiveData("")
    var to_date_str: MutableLiveData<String> = MutableLiveData("")
    var FORMAT_DATE = "yyyy-MM-dd"

    var opening_balance:MutableLiveData<Int> = MutableLiveData(0)
    var closing_balance:MutableLiveData<Int> = MutableLiveData(0)

    var isLoading:MutableLiveData<Boolean> = MutableLiveData(false)

    var cashBookBinder:MutableLiveData<List<CashBookBinder>> = MutableLiveData<List<CashBookBinder>>()

//    var FORMAT_DATE = "dd-MM-yyyy"


    fun CalendarToString(cal: Calendar,format_type:String):String{
        val sdf = SimpleDateFormat(format_type)
        return sdf.format(cal.time)
    }


    fun getCashBookData(from_date_str:String,to_date_str:String,callback:RetrofitCallback){

        isLoading.value = true
        var cashBookRequest = CashBookRequest()
        cashBookRequest.from_date = from_date_str
        cashBookRequest.to_date = to_date_str

        RetrofitClientObj(context).instance.cashBookList(cashBookRequest).enqueue(object :Callback<CashBookResponse>{
            override fun onResponse(
                call: Call<CashBookResponse>,
                response: Response<CashBookResponse>
            ) {
            if (response.isSuccessful){
                isLoading.value = false
                if (response.body() != null){
                    Log.e("priyanka","response.body() = ${response.body()}")

                    opening_balance.value = response.body()!!.opening_balance
                    closing_balance.value = response.body()!!.closing_balance

                    var cashbook_list = response.body()!!.aaData

                    cashBookBinder.postValue(convertModel(cashbook_list))
                }
            }
                else{
                isLoading.value = false
            }
            }
            override fun onFailure(call: Call<CashBookResponse>, t: Throwable) {
                isLoading.value = false
            }
        })
    }

    private fun convertModel(AaDataList: List<AaData>?): List<CashBookBinder>? {

        var cashBookBinderList:ArrayList<CashBookBinder> = ArrayList<CashBookBinder>()

        for (aaData in AaDataList!!){

             var data = aaData.cash_book_list

            for (cashbook in data!!){
              var cashBook = CashBookBinder()
                cashBook.name = cashbook.name
                cashBook.amount = cashbook.amt
                cashBook.type = cashbook.type
                cashBook.date = cashbook.created_at

                cashBookBinderList.add(cashBook)
            }
        }

        return cashBookBinderList

    }


}