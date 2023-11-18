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

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pkt.emarkit.R
import com.pkt.emarkit.api.api_models.DataX
import com.pkt.emarkit.databinding.ActivityPendingBinding
import com.pkt.emarkit.ui.adapter.PendingDeliveryAdapter
import com.pkt.emarkit.ui.interfaces.AlertCallBack
import com.pkt.emarkit.ui.interfaces.CallBack
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewmodels.PendingViewmodel
import java.util.*

class PendingActivity : BaseActivity() {

    lateinit var binding: ActivityPendingBinding
    var Deliveries = arrayListOf<String>("Pending Deliveries","Completed Deliveries","Cancelled Deliveries")
    var AllLocation = arrayListOf<String>("All Location")
    lateinit var deliveries_adapter:ArrayAdapter<String>
    lateinit var location_adapter:ArrayAdapter<String>
    lateinit var pendingViewmodel: PendingViewmodel
    lateinit var adapter:PendingDeliveryAdapter
    var selectedDelivery:String =""
    var Delivery_type = ""
    var Delivery_type1 = ""
    var Delivery_count = 0
    lateinit var menu: Menu
    var menuItemId = 0

    var latitude:Double? = null
    var longitude:Double? = null

    ///////////////////////////////////
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    //////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_pending)



        menu = binding.navigationView.menu
        binding.navigationView.inflateMenu(R.menu.nav_menu)

        menuItemId = R.id.home1
        binding.navigationView.setCheckedItem(menuItemId)
        binding.homeIcon.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)){
                binding.drawerLayout.closeDrawer(Gravity.RIGHT)
            }
            else{
                binding.drawerLayout.openDrawer(Gravity.RIGHT)
            }
        }
        binding.navigationView.setNavigationItemSelectedListener{
            var title = it.title.toString()

            Log.e("priyanka","navigationView clicked")
            Log.e("priyanka","title = $title")

            when(title){

                "Home" ->
                {
                    Log.e("Home","home")
                    finish()
                }


                "Report" ->
                {
                    Log.e("Home","Report")
                    var intent = Intent(this,StockActivity::class.java)
                    startActivity(intent)
                    finish()
                }


                "CashBook" ->
                {
                    Log.e("Home","CashBook")
                    var intent = Intent(this,CashbookActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                "Profile" ->
                {
                    Log.e("Home","profile")
                    var intent = Intent(this,ProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                "Logout" ->
                {
                    Log.e("Home","Logout")
                    showAlert("Logout",
                        "Are you sure you want to logout?",object : AlertCallBack {
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


            //initialize the location services for the location
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //Spinner for deliveries
        deliveries_adapter = ArrayAdapter<String>(this,R.layout.list_item, Deliveries)
        deliveries_adapter.setDropDownViewResource(R.layout.simple_dropdown)
        binding.spinner.adapter = deliveries_adapter

        pendingViewmodel = ViewModelProviders.of(this).get(PendingViewmodel::class.java)

        //call AllLocation Api
        pendingViewmodel.getAreaList(object :RetrofitCallback{
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

        ///////////////////
        pendingViewmodel.location_name.value = AllLocation[0]

        binding.model = pendingViewmodel.selectDeliveryBinder
//        pendingViewmodel.init()
        if (intent.extras != null){
            Delivery_type = intent.extras!!.getString("Delivery_type").toString()
            Log.e("priyanka","Delivery_type = $Delivery_type")
            if (Delivery_type.equals("Pending")){
                binding.spinner.setSelection(0)
            }
            else if (Delivery_type.equals("Completed")){
                binding.spinner.setSelection(1)
            }
            else if (Delivery_type.equals("Cancelled")){
                binding.spinner.setSelection(2)

            }
        }

      //get the user location
        getLocation()
        initRecyclerview()
        initObserver()
        initSearchListener()
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                // Handle item selection here
                Log.e("priyanka","onItemSelected =${Deliveries[position]}")
                selectedDelivery = Deliveries[position]
                Delivery_type1 = Deliveries[position]

                Log.e("priyanka","latitude = $Deliveries[position] = $latitude")
                Log.e("priyanka","longitude = $Deliveries[position] = $longitude")

                pendingViewmodel.getPendingDelivery(pendingViewmodel.location_name.value!!,latitude, longitude,Deliveries[position],object :RetrofitCallback{

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

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when no item is selected
            }
        }
        binding.AllLocation.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                pendingViewmodel.location_name.value = AllLocation[position]
                pendingViewmodel.getPendingDelivery(pendingViewmodel.location_name.value!!,latitude,longitude,selectedDelivery,object :RetrofitCallback{
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

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.imageView.setOnClickListener {
            finish()
        }

    }
    // function getLocation
    @SuppressLint("MissingPermission", "SetTextI18n")
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
                        Log.e("priyanka","Latitude_getLocation11 = ${list[0].latitude}")
                        Log.e("priyanka","Longitude_getLocation11 = ${list[0].longitude}")
                    }
                }
            }
            else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
    }

    private fun initSearchListener() {
        val filter =
            InputFilter { source, start, end, dest, dstart, dend ->
                for (i in start until end) {
                    if (!(Character.isLetterOrDigit(source[i])) && Character.toString(source[i]) != "," && Character.toString(source[i]) != "."
                        && Character.toString(source[i]) != " "
                    ) {
                        return@InputFilter ""
                    }
                }
                null
            }


        binding.editText.filters = arrayOf<InputFilter>(filter)

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {
                    Log.e("priyanka","search calling")
                    pendingViewmodel.selectDeliveryBinder?.searchText?.value = s.toString()
                    pendingViewmodel.getPendingDelivery(pendingViewmodel.location_name.value!!,latitude,longitude,selectedDelivery,object :RetrofitCallback{
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
                }
            }
        })
    }

    private fun initObserver() {

        pendingViewmodel.deliveryItem.observe(this, Observer {
            Log.e("priyanka","deliveryItem = ${it.size}")
            Delivery_count = it.size

            if (Delivery_type1.equals("Pending Deliveries")){
                binding.DeliveryCount.text = "${it.size} Pending Deliveries"
            }

            if (Delivery_type1.equals("Completed Deliveries")){
                binding.DeliveryCount.text = "${it.size} Completed Deliveries"
            }

            if (Delivery_type1.equals("Cancelled Deliveries")){
                binding.DeliveryCount.text = "${it.size} Cancelled Deliveries"
            }

         adapter.submitList(it)
        })
        pendingViewmodel.selectDeliveryBinder.isLoading.observe(this, Observer {
            if (it){
                binding.progressCircular.visibility = View.VISIBLE
            }
            else{
                binding.progressCircular.visibility = View.GONE
            }
        })

        pendingViewmodel.areaList.observe(this, Observer {
            //spinner for all location
            AllLocation.addAll(it)
            location_adapter = ArrayAdapter<String>(this,R.layout.list_item, AllLocation)
            location_adapter.setDropDownViewResource(R.layout.simple_dropdown)
            binding.AllLocation.adapter = location_adapter
        })

    }

    private fun initRecyclerview(){

        adapter = PendingDeliveryAdapter()
        val RVLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        var recyclerView:RecyclerView = binding.recyclerview
        recyclerView.layoutManager = RVLayoutManager
        recyclerView.adapter =adapter

        adapter.initCallback(object :PendingDeliveryAdapter.AdapterInterface{
            @SuppressLint("SuspiciousIndentation")
            override fun gotoDeliveriesPage(order_code:String) {
//                var data:DataX = dataX[position]
              var intent:Intent = Intent(this@PendingActivity,DeliveryActivity::class.java)
                intent.putExtra("order_code",order_code)
                startActivity(intent)
            }
        })
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)){
            binding.drawerLayout.closeDrawer(Gravity.RIGHT)
        }
        else{
            super.onBackPressed()
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    //checking the permission.
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        return false
    }

    // check whether the location is enabled or not
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    //request permission
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), permissionId
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionId){
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
}

