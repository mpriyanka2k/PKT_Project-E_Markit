package com.pkt.emarkit.ui.interfaces

interface PassDatafromActivityToFragment {

    fun onStatusReceived(status: String?)
    fun onSearchReceived(search: String?)
}