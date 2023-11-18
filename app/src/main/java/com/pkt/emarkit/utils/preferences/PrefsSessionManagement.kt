package com.pkt.emarkit.utils.preferences

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.annotation.Keep
import com.pkt.emarkit.db.AppDatabase
import com.pkt.emarkit.ui.interfaces.CallBack
import java.util.Date

@Keep
class PrefsSessionManagement(private val context: Context) {
    private val pref: SharedPreferences
    private var editor: SharedPreferences.Editor? = null
    private val PRIVATE_MODE = 0
    private val PREF_NAME = "Prefs_SessionManagement"
    private val IS_LOGIN = "IsLoggedIn"
    val KEY_ACCESS_TOKEN = "access_token"
    val USER_ID= "user_id"
    val USER_NAME = "user_name"
    val USER_EMAIL = "user_email"
    val Today_Date = "today_date"
    val MOBILE = "mobile_no"
    val PROFILE_IMG = "profile_img"
    val ADDRESS = "address"

    init {
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE)
        editor =pref.edit()

    }

    fun logoutUserSession(callback: CallBack) {
        Log.d("PrefsSessionManagement","logout")
        try
        {
            editor!!.clear()
            editor!!.commit()

            deleteAllDatabaseTable()
            callback.onSuccess()
        }
        catch (e:Exception)
        {

        }

    }

    fun createLoginSession(
        access_token: String,
        userId: Int,
        name: String?,
        email: String,
        mobile:String,
        profileImg:String?,
        address:String
    ){
     editor!!.putBoolean(IS_LOGIN,true)
     editor!!.putString(KEY_ACCESS_TOKEN,access_token)
     editor!!.putInt(USER_ID, userId)
     editor!!.putString(USER_NAME,name)
     editor!!.putString(USER_EMAIL,email)
     editor!!.putString(MOBILE ,mobile)
     editor!!.putString(PROFILE_IMG,profileImg)
     editor!!.putString(ADDRESS,address)
     editor!!.commit()
    }

    fun getProfileImg(): String? {
        return pref.getString(PROFILE_IMG,null)
    }
    fun getUserName():String?{
        return pref.getString(USER_NAME,"")
    }
    fun getMobileNo():String?{
        return pref.getString(MOBILE,"")
    }
    fun getEmailId():String?{
        return pref.getString(USER_EMAIL,"")
    }
    fun getAddress():String?{
        return pref.getString(ADDRESS,"")
    }
    fun getAccessToken():String?{
        return pref.getString(KEY_ACCESS_TOKEN,"")
    }

    fun isLoggedIn():Boolean{
        return pref.getBoolean(IS_LOGIN,false)
    }

    fun setTodayDate(date:String){
        editor!!.putString(Today_Date,date)
    }

    fun getTodayDate():String?{
        return pref.getString(Today_Date,"")
    }

    fun deleteAllDatabaseTable(){
      Thread(Runnable {
         AppDatabase.getDatabase(context).clearAllTables()
      }).start()

        Log.e("priyanka","table deleted successfully")
    }
}