package com.pkt.emarkit.ui.interfaces

import android.content.DialogInterface

interface AlertCallBack {

    fun positiveClicked(builder: DialogInterface)
    fun negativeClicked(builder: DialogInterface)
}