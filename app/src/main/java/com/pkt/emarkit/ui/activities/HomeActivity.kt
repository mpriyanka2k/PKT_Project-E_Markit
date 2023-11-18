package com.pkt.emarkit.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pkt.emarkit.R

import com.pkt.emarkit.databinding.ActivityHomeBinding
import com.pkt.emarkit.ui.interfaces.AlertCallBack
import com.pkt.emarkit.ui.interfaces.CallBack
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewmodels.HomeViewmodel

import java.util.*

class HomeActivity : BaseActivity() {

    lateinit var bottom_nav:BottomNavigationView
    lateinit var homeViewmodel: HomeViewmodel
    var menuItemId = 0
    private val permissionId = 3
    lateinit var menu: Menu
    var latitude:Double? = null
    var longitude:Double? = null
    lateinit var mFusedLocationClient: FusedLocationProviderClient


    lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        homeViewmodel = ViewModelProviders.of(this).get(HomeViewmodel::class.java)
        binding.lifecycleOwner = this
        binding.viewBinder = homeViewmodel.dashboardBinder

        menu = binding.navigationView.menu
        binding.navigationView.inflateMenu(R.menu.nav_menu)

        menuItemId = R.id.home1
        binding.navigationView.setCheckedItem(menuItemId)

        binding.navigationView.setNavigationItemSelectedListener{
            var title = it.title.toString()

            Log.e("priyanka","navigationView clicked")
            Log.e("priyanka","title = $title")

         when(title){

         "Home" ->
         {
             Log.e("Home","home")
         }


         "Report" ->
         {
             Log.e("Home","Report")
             var intent = Intent(this,StockActivity::class.java)
             startActivity(intent)
         }


         "CashBook" ->
         {
             Log.e("Home","CashBook")
             var intent = Intent(this,CashbookActivity::class.java)
             startActivity(intent)
         }

             "Profile" ->
             {
                 Log.e("Home","profile")
                 var intent = Intent(this,ProfileActivity::class.java)
                 startActivity(intent)
             }

             "Logout" ->
         {
             Log.e("Home","Logout")
             showAlert("Logout",
                 "Are you sure you want to logout?",object :AlertCallBack{
                     override fun positiveClicked(builder: DialogInterface) {
                         builder.dismiss()
                         session.logoutUserSession(object : CallBack {
                             override fun onSuccess() {

                                 val new_intent = Intent(context, LoginActivity::class.java)
                                 new_intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                 startActivity(new_intent)
                                 finish()



                             }

                             override fun onFail() {

                             }

                         })
                     }

                     override fun negativeClicked(builder: DialogInterface) {

                     }

                 })

         }

         }
            binding.drawerLayout.closeDrawer(Gravity.RIGHT)
            true
        }




        initObserver()
        homeViewmodel.getDashboardData()

        homeViewmodel.stockData(object :RetrofitCallback{
            override fun onSuccess() {
                showShortToast("onSuccess")
            }

            override fun onFail(message: String) {
                showShortToast(message)
            }

            override fun onApiFail(message: Throwable) {
                RetrofitExceptionHandling(null,message)
            }

            override fun onApiUnsuccessResponse(responseCode: Int) {
                RetrofitUnSuccessfulResponse(responseCode)
            }

        })

        //initialize the location services for the location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //get the user location
        getLocation()

        //set selected fragment
//      changeFragment(homeFragment.newInstance())

        //open Pending
        binding.linearPending.setOnClickListener {
            var intent = Intent(context,PendingActivity::class.java)
            intent.putExtra("Delivery_type","Pending")
            startActivity(intent)
        }

        //open Completed
        binding.linearCompleted.setOnClickListener {
            var intent = Intent(context,PendingActivity::class.java)
            intent.putExtra("Delivery_type","Completed")
            startActivity(intent)
        }

        //open cancelled
        binding.linearCancelled.setOnClickListener {
            var intent = Intent(context,PendingActivity::class.java)
            intent.putExtra("Delivery_type","Cancelled")
            startActivity(intent)
        }


        bottom_nav = findViewById(R.id.bottom_nav)
        bottom_nav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home ->{
//                    changeFragment(homeFragment.newInstance())
                    true
                }
                R.id.reports ->{
                    var intent = Intent(this,StockActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.cashbook ->{
                    var intent = Intent(this,CashbookActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> {
                    true
                }
            }
        }

        binding.homeIcon.setOnClickListener {
         if (binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)){
             binding.drawerLayout.closeDrawer(Gravity.RIGHT)
         }
            else{
            binding.drawerLayout.openDrawer(Gravity.RIGHT)
         }
        }
    }

    override fun onBackPressed() {
     if(binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)){
        binding.drawerLayout.closeDrawer(Gravity.RIGHT)
     }
        else{
         super.onBackPressed()
     }
    }

    override fun onResume() {
        super.onResume()
        Log.e("priyanka","onResume")
        bottom_nav.setSelectedItemId(R.id.home);
        binding.navigationView.setCheckedItem(menuItemId)
    }

    private fun initObserver() {
       homeViewmodel.loading.observe(this, androidx.lifecycle.Observer {
           if (it){
               binding.progressbar.visibility = View.VISIBLE
           }
           else{
               binding.progressbar.visibility = View.GONE
           }
       })
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)!!

                        latitude = list[0].latitude
                        longitude = list[0].longitude
                        Log.e("priyanka","Latitude_getLocation = ${list[0].latitude}")
                        Log.e("priyanka","Longitude_getLocation = ${list[0].longitude}")
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
    }

    //request the permission.
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), permissionId
        )
    }
    //checking the permission.
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        return false
    }
//   // check whether the location is enabled or not
   private fun isLocationEnabled(): Boolean {
       val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
       return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
   }

}