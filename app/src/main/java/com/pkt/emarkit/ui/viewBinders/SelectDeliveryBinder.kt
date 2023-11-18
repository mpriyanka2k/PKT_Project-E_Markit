package com.pkt.emarkit.ui.viewBinders

import androidx.lifecycle.MutableLiveData

class SelectDeliveryBinder {
    var searchText:MutableLiveData<String> = MutableLiveData("")
    var isLoading = MutableLiveData(false)


}