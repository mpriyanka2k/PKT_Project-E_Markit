package com.pkt.emarkit.ui.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pkt.emarkit.R
import com.pkt.emarkit.api.api_models.DataX
import com.pkt.emarkit.databinding.ActivityDeliveryBinding
import com.pkt.emarkit.db.data_models.Delivery
import com.pkt.emarkit.ui.adapter.ProductItemAdapter
import com.pkt.emarkit.ui.interfaces.AlertCallBack
import com.pkt.emarkit.ui.interfaces.CallBack
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewBinders.ProductItemBinder
import com.pkt.emarkit.ui.viewmodels.DeliveryViewmodel

class DeliveryActivity : BaseActivity() {

    lateinit var binding: ActivityDeliveryBinding
    lateinit var deliveryViewmodel: DeliveryViewmodel
    lateinit var adapter:ProductItemAdapter
    var order_code = ""
    lateinit  var datax:DataX;
    lateinit var delivery:Delivery
    lateinit var menu: Menu
    var menuItemId = 0

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_delivery)
        binding.lifecycleOwner = this
        deliveryViewmodel = ViewModelProviders.of(this).get(DeliveryViewmodel::class.java)

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
            order_code = intent.getStringExtra("order_code")!!
//            datax = intent.getParcelableExtra<DataX>("Datax")!!
//            datax = intent.getSerializableExtra("Datax") as DataX
//            Log.e("priyanka","order_code2 = ${datax.delivery_code}")
            Log.e("priyanka","order_code = $order_code")

        }
        binding.model = deliveryViewmodel.deliveryItemBinder

        initRecyclerview()
        initObserver()

        binding.imageView.setOnClickListener {
            finish()
        }

        binding.CancleBtn.setOnClickListener {

            var mDialogview = LayoutInflater.from(context).inflate(R.layout.custom_popup,null)
            var mBuilder = AlertDialog.Builder(this)
                .setView(mDialogview)
            var mAlertDialog = mBuilder.show()

            var submitbtn = mDialogview.findViewById<TextView>(R.id.submit)
            var backbtn = mDialogview.findViewById<TextView>(R.id.back)
            var reasontxt = mDialogview.findViewById<EditText>(R.id.Reason)

            submitbtn.setOnClickListener {

                mAlertDialog.dismiss()
                var reason = reasontxt.text.toString()
                Log.e("priyanka","reason = $reason")

                //call cancel delivery
                deliveryViewmodel.cancelDelivery(order_code,reason,object : RetrofitCallback{
                    override fun onSuccess() {
                        var intent = Intent(this@DeliveryActivity,HomeActivity::class.java)
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
            backbtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        deliveryViewmodel.deliveryData(order_code).observe(this, Observer {
//            Log.e("priyanka","detail = $it")
            deliveryViewmodel.delivery.value = it
            deliveryViewmodel.setData()

            binding.textView5.text = deliveryViewmodel.deliveryItemBinder.product_name
            binding.invoice.text = deliveryViewmodel.deliveryItemBinder.invoice
            binding.totalAmt.text = deliveryViewmodel.deliveryItemBinder.total_amt
            binding.date.text = deliveryViewmodel.deliveryItemBinder.date
            binding.address.text = deliveryViewmodel.deliveryItemBinder.address
        })

        binding.deliverBtn.setOnClickListener {
            var intent = Intent(this,OrderSubmitActivity::class.java)
            intent.putExtra("Total_Amount",deliveryViewmodel.total_amount_with_Rs.value)
            intent.putExtra("Date",deliveryViewmodel.deliveryItemBinder.date)
            intent.putExtra("order_code",order_code)
            intent.putExtra("productitem",ArrayList(deliveryViewmodel.productItemParceble.value))
            intent.putExtra("delivery",deliveryViewmodel.delivery.value)
//            intent.putExtra("productItem",deliveryViewmodel.productItem.value)
            startActivity(intent)
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

    private fun initObserver(){
        deliveryViewmodel.productItem.observe(this, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()

            var list: List<ProductItemBinder>? = it
//            productItem.postValue(convertModel(details))

            deliveryViewmodel.productItemParceble.postValue(deliveryViewmodel.convertModelParceble(it))
        })

        deliveryViewmodel.total_item.observe(this, Observer {
            binding.totalItems.text = "$it Items"
        })

        deliveryViewmodel.total_amount_with_Rs.observe(this, Observer {
            binding.totalAmt.text = "Rs.$it"
        })

//        deliveryViewmodel.produvtItemBinder.total_qty.observe(this, Observer {
//            Log.e("priyanka","total_qty11 = $it")
//        })
    }

    private fun initRecyclerview() {
        adapter = ProductItemAdapter()
        val RVLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        var recyclerView:RecyclerView = binding.recyclerview
        recyclerView.layoutManager = RVLayoutManager
        recyclerView.adapter = adapter

        adapter.initCallback(object :ProductItemAdapter.AdapterInterface{
            override fun increase(position:Int,available_qty: Int?,single_product_amount:Int) {
                deliveryViewmodel.increase(position,available_qty,single_product_amount)
                adapter.notifyDataSetChanged()

                /////////////////////////////////////////
                deliveryViewmodel.productItem.observe(this@DeliveryActivity, Observer {
                  Log.e("priyanka","increase")
                    deliveryViewmodel.productItemParceble.postValue(deliveryViewmodel.convertModelParceble(it))

                    for (item in it){
                        Log.e("priyanka","item 1== ${item.total_qty1}")
                        Log.e("priyanka","item 2== ${item.total_qty}")
                        Log.e("priyanka","item 3== ${item.available_qty}")
                    }

                })
                ////////////////////////////////////////
            }

            override fun decrease(position:Int,available_qty: Int?,single_product_amount:Int) {
                deliveryViewmodel.decrease(position,available_qty,single_product_amount)
                adapter.notifyDataSetChanged()

                /////////////////////////////////////////
                deliveryViewmodel.productItem.observe(this@DeliveryActivity, Observer {
                    Log.e("priyanka","decrease")
                    deliveryViewmodel.productItemParceble.postValue(deliveryViewmodel.convertModelParceble(it))

                    for (item in it){
                        Log.e("priyanka","item 11== ${item.total_qty1}")
                        Log.e("priyanka","item 22== ${item.total_qty}")
                        Log.e("priyanka","item 33== ${item.available_qty}")
                    }
                })
                ////////////////////////////////////////

            }

        })
    }
}