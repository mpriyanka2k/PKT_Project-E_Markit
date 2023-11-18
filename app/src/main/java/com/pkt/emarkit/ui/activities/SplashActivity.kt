package com.pkt.emarkit.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.pkt.emarkit.R
import com.pkt.emarkit.utils.preferences.PrefsSessionManagement
import java.util.*

class SplashActivity : AppCompatActivity() {

    lateinit var context: Context
    private val splash_time = 2000L
    private lateinit var myHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        context = this
        myHandler = Handler()

        var session = PrefsSessionManagement(context)

        Log.e("priyanka","session.isLoggedIn() = ${session.isLoggedIn()}")

        if (session.isLoggedIn()){
            myHandler.postDelayed({
                var intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finish()
            },splash_time)
        }
        else{
            myHandler.postDelayed({
                var intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            },splash_time)

        }

        //checked current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // add 1 because months are indexed from 0
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var current_date = "$year/$month/$day"
        Log.e("priyanka","current_date = $current_date")

        var SharedPreferences_date = session.getTodayDate()
        Log.e("priyanka","SharedPreferences_date = $SharedPreferences_date")

        if (!current_date.equals(SharedPreferences_date)){
            //Delete all database table
            session.deleteAllDatabaseTable()
            //And set SharedPreferences_date = current_date
            session.setTodayDate(current_date)
        }
    }
}