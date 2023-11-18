package com.pkt.emarkit.ui.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.google.firestore.v1.StructuredQuery.Order
import com.pkt.emarkit.R
import com.pkt.emarkit.databinding.ActivityOrderSubmitBinding
import com.pkt.emarkit.db.data_models.Delivery
import com.pkt.emarkit.ui.interfaces.AlertCallBack
import com.pkt.emarkit.ui.interfaces.CallBack
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewBinders.ProductItemBinder
import com.pkt.emarkit.ui.viewBinders.ProductItemParceble
import com.pkt.emarkit.ui.viewmodels.OrderSubmitViewmodel

class OrderSubmitActivity : BaseActivity() {
    lateinit var binding:ActivityOrderSubmitBinding
    lateinit var orderSubmitViewmodel: OrderSubmitViewmodel
    lateinit var productitem:ArrayList<ProductItemParceble>
    lateinit var delivery: Delivery
    var Final_Total_Amount = 0
    var cash_amount_editable = 0
    var upi_amount_editable = 0
    var upi_amount = 0
    var cash_amount= 0
    var date = ""
    var order_code = ""
    var total_amount = 0
    lateinit var menu: Menu
    var menuItemId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_submit)
        binding.lifecycleOwner = this

        orderSubmitViewmodel = ViewModelProviders.of(this).get(OrderSubmitViewmodel::class.java)

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
                    var intent = Intent(this,HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
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
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent)
                    finish()
                }

                "Profile" ->
                {
                    Log.e("Home","profile")
                    var intent = Intent(this,ProfileActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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


        if (intent.extras != null){
            orderSubmitViewmodel.Total_amount.value = intent.getIntExtra("Total_Amount",0)

            orderSubmitViewmodel.Due_amount.value = intent.getIntExtra("Total_Amount",0)

            total_amount = intent.getIntExtra("Total_Amount",0)
            Log.e("priyanka","total_amount is = $total_amount")

            date = intent.getStringExtra("Date")!!

            order_code = intent.getStringExtra("order_code")!!


            Final_Total_Amount = intent.getIntExtra("Total_Amount",0)
            Log.e("priyanka","Total_Amount = $Final_Total_Amount")

            productitem = intent.getParcelableArrayListExtra<ProductItemParceble>("productitem") as ArrayList<ProductItemParceble>
            delivery = intent.getParcelableExtra<Delivery>("delivery")!!

            Log.e("priyanka","productitem = ${productitem}")
            Log.e("priyanka","delivery = ${delivery}")
            Log.e("priyanka","delivery_item = ${delivery.details}")

        }

        binding.date.text = date
        binding.imageView4.setOnClickListener {
            finish()
        }

        binding.submit.setOnClickListener {

            if(upi_amount > total_amount){
                showShortToast("UPI amount is greater than total amount")
                Log.e("priyanka","upi_amount_editable = $upi_amount_editable")
                Log.e("priyanka","total_amount = $total_amount")
            }

            else if (cash_amount > total_amount){
                showShortToast("Cash amount is greater than total amount")
                Log.e("priyanka","cash_amount = $cash_amount_editable")
                Log.e("priyanka","total_amount = $total_amount")
            }
            else{
                orderSubmitViewmodel.confirmDelivery(delivery,productitem,order_code,total_amount,orderSubmitViewmodel.received_amt.value.toString(),orderSubmitViewmodel.Due_amount.value.toString(),object :RetrofitCallback{
                    override fun onSuccess() {

                        var intent = Intent(this@OrderSubmitActivity,HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
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

        initObserver()
        initCashAmountChangelistener()
        initUpiPaymentChangelistener()
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)){
            binding.drawerLayout.closeDrawer(Gravity.RIGHT)
        }
        else{
            super.onBackPressed()
        }
    }

    private fun initCashAmountChangelistener() {
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
        binding.CashAmount.filters = arrayOf<InputFilter>(filter)
        binding.CashAmount.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length >0){
                    Log.e("priyanka","search $s")
                    cash_amount = s.toString().toInt()

                    if (s.toString().toInt() <= total_amount){
                        cash_amount_editable  = s.toString().toInt()

                        Log.e("priyanka","cash_amount_editable = $cash_amount_editable")

                        orderSubmitViewmodel.Cash.value = cash_amount_editable
                    }
                    else{
                        showShortToast("Cash amount is greater than total amount")
                    }


                }
                if (s.length == 0){
                    orderSubmitViewmodel.Cash.value = 0
                    cash_amount_editable = 0
                }
            }
        })
    }
    private fun initUpiPaymentChangelistener() {
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
        binding.uipAmount.filters = arrayOf<InputFilter>(filter)
        binding.uipAmount.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length >0){
                   upi_amount = s.toString().toInt()

                    if (s.toString().toInt() <= total_amount){
                        upi_amount_editable  = s.toString().toInt()

                        Log.e("priyanka","cash_amount_editable = $upi_amount_editable")

                        orderSubmitViewmodel.UPI.value = upi_amount_editable
                    }
                    else{
                        showShortToast("UPI amount is greater than total amount")
                    }
                }
                if (s.length == 0){
                    orderSubmitViewmodel.UPI.value = 0
                    upi_amount_editable = 0
                }
            }
        })
    }
    private fun initObserver() {
        orderSubmitViewmodel.Total_amount.observe(this, Observer {
            binding.totalAmount.text = "Rs $it"
        })
        orderSubmitViewmodel.Due_amount.observe(this, Observer {
            binding.DueAmount.text = "Rs $it"

            orderSubmitViewmodel.received_amt.value = total_amount - it
        })
        orderSubmitViewmodel.Cash.observe(this, Observer {

            Log.e("priyanka","cash $it")

            orderSubmitViewmodel.Due_amount.value = Final_Total_Amount - it - upi_amount_editable

        })
        orderSubmitViewmodel.UPI.observe(this, Observer {

            Log.e("priyanka","UPI $it")

            orderSubmitViewmodel.Due_amount.value = Final_Total_Amount - it - cash_amount_editable

        })
    }
}