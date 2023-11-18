package com.pkt.emarkit.ui.activities

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pkt.emarkit.R
import com.pkt.emarkit.databinding.ActivityCashbookBinding
import com.pkt.emarkit.ui.adapter.CashBookAdapter
import com.pkt.emarkit.ui.interfaces.AlertCallBack
import com.pkt.emarkit.ui.interfaces.CallBack
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewmodels.CashBookViewmodel
import java.util.*


class CashbookActivity : BaseActivity(){

    lateinit var cashBookViewmodel: CashBookViewmodel
    lateinit var binding:ActivityCashbookBinding
    lateinit var adapter: CashBookAdapter
    lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_cashbook)

        binding.lifecycleOwner = this

        cashBookViewmodel = ViewModelProviders.of(this).get(CashBookViewmodel::class.java)

        menu = binding.navigationView.menu
        binding.navigationView.inflateMenu(R.menu.nav_menu)
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
        var menuItemId = R.id.cashbook1
        binding.navigationView.setCheckedItem(menuItemId)

        initRecyclerview()
        initObserver()

        cashBookViewmodel.getCashBookData(cashBookViewmodel.from_date_str.value!!,cashBookViewmodel.to_date_str.value!!,object :RetrofitCallback{
            override fun onSuccess() {
             Log.e("priyanka","onSuccess")
            }

            override fun onFail(message: String) {
                Log.e("priyanka","onFail")
            }

            override fun onApiFail(message: Throwable) {
                Log.e("priyanka","onApiFail")
            }

            override fun onApiUnsuccessResponse(responseCode: Int) {
                Log.e("priyanka","onApiUnsuccessResponse")
            }
        })
        binding.selectFromDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    var calender = Calendar.getInstance()
                    calender.set(year,month,dayOfMonth)

                    cashBookViewmodel.from_date.value = calender

                    cashBookViewmodel.from_date_str.value = cashBookViewmodel.CalendarToString(calender,cashBookViewmodel.FORMAT_DATE)
                }
            },  cashBookViewmodel.from_date.value!!.get(Calendar.YEAR),  cashBookViewmodel.from_date.value!!.get(Calendar.MONTH),  cashBookViewmodel.from_date.value!!.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()

        }

        binding.selectToDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    var calender = Calendar.getInstance()
                    calender.set(year,month,dayOfMonth)
                    cashBookViewmodel.to_date.value = calender
                    cashBookViewmodel.to_date_str.value = cashBookViewmodel.CalendarToString(calender,cashBookViewmodel.FORMAT_DATE)

                }
            },cashBookViewmodel.to_date.value!!.get(Calendar.YEAR), cashBookViewmodel.to_date.value!!.get(Calendar.MONTH), cashBookViewmodel.to_date.value!!.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
            datePickerDialog.datePicker.minDate = cashBookViewmodel.from_date.value!!.timeInMillis
            datePickerDialog.show()
        }

        binding.imageView.setOnClickListener {
//            var intent = Intent(this,HomeActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
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

    private fun initRecyclerview() {
        adapter = CashBookAdapter()
        val RVLayoutManager:RecyclerView.LayoutManager = LinearLayoutManager(context)
        var recyclerView:RecyclerView = binding.recyclerview
        recyclerView.layoutManager = RVLayoutManager
        recyclerView.adapter = adapter
    }

    private fun initObserver() {
        cashBookViewmodel.from_date_str.observe(this, androidx.lifecycle.Observer {
            Log.e("priyanka","from_date_str = $it")
            if (it.equals("")){
                binding.fromDate.text = "From"
            }
            else{
                binding.fromDate.text = it
            }
            //////////////////////////////////////////////////////////////////////////
            // call cashbook api
            cashBookViewmodel.getCashBookData(cashBookViewmodel.from_date_str.value!!,
                cashBookViewmodel.to_date_str.value!!,object :RetrofitCallback{
                    override fun onSuccess() {
                    }
                    override fun onFail(message: String) {
                    }
                    override fun onApiFail(message: Throwable) {
                    }
                    override fun onApiUnsuccessResponse(responseCode: Int) {
                    }
                })
            //////////////////////////////////////////////////////////////////////////
        })

        cashBookViewmodel.to_date_str.observe(this, androidx.lifecycle.Observer {
            Log.e("priyanka","to_date_str = $it")
            if (it.equals("")){
                binding.toDate.text = "to"
            }
            else{
                binding.toDate.text = it
            }

            //////////////////////////////////////////////////////////////////////////
            // call cashbook api
            cashBookViewmodel.getCashBookData(cashBookViewmodel.from_date_str.value!!,
                cashBookViewmodel.to_date_str.value!!,object :RetrofitCallback{
                    override fun onSuccess() {

                    }

                    override fun onFail(message: String) {

                    }

                    override fun onApiFail(message: Throwable) {

                    }

                    override fun onApiUnsuccessResponse(responseCode: Int) {

                    }

                })
            //////////////////////////////////////////////////////////////////////////
        })

        cashBookViewmodel.opening_balance.observe(this, androidx.lifecycle.Observer {
            binding.openingBalance.text = it.toString()
        })

        cashBookViewmodel.closing_balance.observe(this, androidx.lifecycle.Observer {
            binding.closingBalance.text = it.toString()
        })

        cashBookViewmodel.isLoading.observe(this, androidx.lifecycle.Observer {
            if (it){
                binding.progressBar.visibility = View.VISIBLE
            }

            else{
                binding.progressBar.visibility = View.GONE
            }
        })

        cashBookViewmodel.cashBookBinder.observe(this, androidx.lifecycle.Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })
    }

}