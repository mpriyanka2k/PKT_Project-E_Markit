package com.pkt.emarkit.ui.interfaces

import com.pkt.emarkit.api.api_models.LoginResponse

interface LoginCallbackInterface {

    fun onSuccess()
    fun onFail(message:String)
    fun onApiFail(message:Throwable)
    fun onApiUnsuccessResponse(responseCode:Int)
}