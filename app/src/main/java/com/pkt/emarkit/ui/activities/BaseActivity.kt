package com.pkt.emarkit.ui.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.pkt.emarkit.R
import com.pkt.emarkit.ui.interfaces.AlertCallBack
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.utils.preferences.PrefsSessionManagement
import java.net.SocketTimeoutException

open class BaseActivity : AppCompatActivity() {

    lateinit var progressDialog: ProgressDialog
    lateinit var context: Context
    lateinit var activity: Activity
    lateinit var session:PrefsSessionManagement
    lateinit var ui_handler:Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        context = this
        activity = this
        session = PrefsSessionManagement(this)
        progressDialog = ProgressDialog(this)
        ui_handler = Handler(context.mainLooper)

    }
    fun showProgress(){
       progressDialog.setMessage("Please wait...")
       progressDialog.setCancelable(false)
       progressDialog.show()
    }
    fun dismissProgress(){
       progressDialog.dismiss()
    }

    open fun showShortToast(msg:String){
     if (msg.length > 0){
         Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
     }
    }

    open fun showLongToast(msg:String){
        ui_handler.post(Runnable {
         Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
        })
    }

    open fun RetrofitUnSuccessfulResponse(code:Int):String{
        when(code){
         500 ->{
           showShortToast("Something went wrong..Please try after some time")
           return "Something went wrong..Please try after some time"
         }
         422 ->{
             showShortToast("Improper data")
             return "Improper data"
         }
         401 ->{
             showShortToast("Session Expired")
             return "Session Expired"
         }
         else ->{
             showShortToast("Something went wrong..Please try after some time")
             return "Something went wrong..Please try after some time"
         }
        }
    }

    open fun RetrofitExceptionHandling(callback: RetrofitCallback?, t: Throwable) {
        showShortToast(getApiFailError(t))
        if (callback != null) {
            callback.onFail(t.message!!)

        }
    }

    open fun getApiFailError(t: Throwable): String {
        var message = ""
        message = if (!isInternetAvailable(context)!!) {
            "Cannot connect to internet...Please check your connection"
        } else if (t is SocketTimeoutException) {
            "Timeout"
        } else {
          "Something wend wrong ${t.message}"
        }
        return message
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    open fun showAlert(title: String?, message: String?, alertDialogInterface: AlertCallBack) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message) // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(
                "YES"
            ) { dialog, which -> // Continue with delete operation
                alertDialogInterface.positiveClicked(dialog)
            } // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton("NO", null)
            .show()
    }

}