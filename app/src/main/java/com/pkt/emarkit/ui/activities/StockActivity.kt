package com.pkt.emarkit.ui.activities

import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.pkt.emarkit.R
import com.pkt.emarkit.databinding.ActivityStockBinding
import com.pkt.emarkit.ui.adapter.TabAdapter
import com.pkt.emarkit.ui.interfaces.AlertCallBack
import com.pkt.emarkit.ui.interfaces.CallBack
import com.pkt.emarkit.ui.interfaces.PassDatafromActivityToFragment
import com.pkt.emarkit.ui.interfaces.PassDatafromActivitytoFragment1
import com.pkt.emarkit.ui.viewmodels.StockViewmodel


class StockActivity : BaseActivity() {

    lateinit var binding:ActivityStockBinding
//    var Stock = arrayListOf<String>("Pending Stock","Received Stock")
    var Stock = arrayListOf<String>("Pending Stock","Received Stock","Delivered Stock")
    lateinit var stockType_adapter:ArrayAdapter<String>
    lateinit var stockViewmodel: StockViewmodel
    lateinit var menu: Menu

    lateinit var passDatafromActivityToFragment:PassDatafromActivityToFragment
    lateinit var passDatafromActivityToFragment1: PassDatafromActivitytoFragment1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_stock)
        stockViewmodel = ViewModelProviders.of(this).get(StockViewmodel::class.java)

        //Spinner for stockType
        stockType_adapter = ArrayAdapter<String>(this,R.layout.list_item, Stock)
        stockType_adapter.setDropDownViewResource(R.layout.simple_dropdown)
        binding.spinner.adapter = stockType_adapter

        stockViewmodel.typeWise.value = "Item Wise"

        menu = binding.navigationView.menu

        binding.navigationView.inflateMenu(R.menu.nav_menu)

        var menuItemId = R.id.reports1
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


        initObserver()
        initSearchListener()

        binding.spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                if(Stock[position].equals("Pending Stock")){
                    stockViewmodel.stockType.value = "1"
                }
                if (Stock[position].equals("Received Stock")){
                    stockViewmodel.stockType.value = "2"
                }
                if (Stock[position].equals("Delivered Stock")){
                    stockViewmodel.stockType.value ="3"
                }
//                stockViewmodel.stockType.value = Stock[position]
                Log.e("priyanka"," Stock[position] = ${Stock[position]}")
//                sendStatusToFragment(Stock[position])
                sendStatusToFragment(stockViewmodel.stockType.value!!)
                //Call the stock api
            }

            override fun onNothingSelected(parent: AdapterView<*>?)
            {

            }
        }

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Item Wise"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Party Wise"))

        binding.tabLayout.setTabTextColors(Color.parseColor("#939796"),Color.parseColor("#FF000000"))

        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = TabAdapter(this, supportFragmentManager,binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter

        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position

                stockViewmodel.typeWise.value = tab.text.toString()
                //Call the stock api

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.imageView.setOnClickListener {
            onBackPressed()
        }


    }

    override fun onBackPressed() {

//        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val runningTasks = activityManager.getRunningTasks(1)
//        if(binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)){
//            binding.drawerLayout.closeDrawer(Gravity.RIGHT)
//        }
//        else{
////            super.onBackPressed()
//            ////////////////////////////////
//            if (runningTasks.isNotEmpty()) {
//                val topActivity = runningTasks[0].topActivity
//                Log.e("priyanka","topactivity if")
//
//                if (topActivity!!.packageName == packageName && topActivity.className == this.javaClass.name) {
//                    // Activity back stack is empty
//                    // Perform your desired action
//                    var intent = Intent(this,HomeActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                } else {
//                    // Activity back stack is not empty
//                    // Perform your desired action
//                    super.onBackPressed()
//                }
//            }
//            else {
//                // No running tasks
//                // Perform your desired action
//                Log.e("priyanka","topactivity else")
//            }
//            ////////////////////////////////
//        }

        if(binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)){
            binding.drawerLayout.closeDrawer(Gravity.RIGHT)
        }
        else{
//            super.onBackPressed()
            var intent = Intent(this,HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private fun initSearchListener() {
        val filter = InputFilter{source, start, end, dest, dstart, dend ->
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

        binding.editText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
              if (s!!.length > 2){

                  Log.e("priyanka","search calling")

                  sendSearchToFragment(s.toString())

              }
            }

        })
    }

    private fun initObserver() {
//        stockViewmodel.stockType.observe(this, Observer {
//            Log.e("priyanka","stockType   = $it")
//        })
        stockViewmodel.typeWise.observe(this, Observer {
            Log.e("priyanka","typeWise  = $it")
        })

        stockViewmodel.partyWiseProductBinder.observe(this, Observer {
            Log.e("priyanka","partyWiseProductBinder size = ${it.size}")
        })

        stockViewmodel.itemWiseProductBinder.observe(this, Observer {
            Log.e("priyanka","itemWiseProductBinder size = ${it.size}")
        })
    }


    fun setNotificationListener(passDatafromActivityToFragment:PassDatafromActivityToFragment) {
        this.passDatafromActivityToFragment = passDatafromActivityToFragment
    }

    fun setNotificationListener1(passDatafromActivitytoFragment1: PassDatafromActivitytoFragment1){
        this.passDatafromActivityToFragment1 = passDatafromActivitytoFragment1
    }
    // Method to send the notification to the fragment
    private fun sendStatusToFragment(status: String) {
        if (passDatafromActivityToFragment != null) {
            passDatafromActivityToFragment.onStatusReceived(status)
        }
        if (passDatafromActivityToFragment1 != null){
            passDatafromActivityToFragment1.onStatusReceived(status)
        }
    }

    private fun sendSearchToFragment(search: String) {
        if (passDatafromActivityToFragment != null) {
            passDatafromActivityToFragment.onSearchReceived(search)
        }
        if (passDatafromActivityToFragment1 != null){
            passDatafromActivityToFragment1.onSearchReceived(search)
        }
    }
}