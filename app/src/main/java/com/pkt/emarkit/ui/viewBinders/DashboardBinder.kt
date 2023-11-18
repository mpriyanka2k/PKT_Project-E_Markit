package com.pkt.emarkit.ui.viewBinders

import androidx.lifecycle.MutableLiveData

class DashboardBinder {

    var pending_count:MutableLiveData<String> = MutableLiveData("0")
    var cancelled_count:MutableLiveData<String> = MutableLiveData("0")
    var completed_count:MutableLiveData<String> = MutableLiveData("0")
//    var loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)

    var stock_balance:MutableLiveData<String> = MutableLiveData("")
    var stock_returned:MutableLiveData<String> = MutableLiveData("")
    var stock_ledger:MutableLiveData<String> = MutableLiveData("")

}