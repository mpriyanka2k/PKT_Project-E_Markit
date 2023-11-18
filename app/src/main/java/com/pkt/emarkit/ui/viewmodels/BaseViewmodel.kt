package com.pkt.emarkit.ui.viewmodels

import android.app.Application
import android.app.ProgressDialog
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.pkt.emarkit.db.AsyncDbCrud
import java.net.SocketTimeoutException

public open class BaseViewmodel(application: Application):AndroidViewModel(Application()) {

    var context = application
    companion object{
        var mProgressDialog: ProgressDialog? = null
    }

//   var asyncDbCrud:AsyncDbCrud = AsyncDbCrud(context)
   private var ui_handler:Handler? = Handler(context.mainLooper)

    fun showToast(message: String)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    open fun dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog?.dismiss()
            mProgressDialog = null
        }
    }

    open fun showProgress(msg: String?, isCancellable: Boolean) {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) dismissProgress()
        mProgressDialog = ProgressDialog(context)
        mProgressDialog?.setMessage(msg)
        mProgressDialog?.setCancelable(isCancellable)
        mProgressDialog?.setCanceledOnTouchOutside(false)
        mProgressDialog?.show()
    }





}