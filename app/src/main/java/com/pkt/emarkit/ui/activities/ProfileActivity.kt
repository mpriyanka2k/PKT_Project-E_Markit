package com.pkt.emarkit.ui.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.pkt.emarkit.R
import com.pkt.emarkit.databinding.ActivityProfileBinding
import com.pkt.emarkit.ui.interfaces.AlertCallBack
import com.pkt.emarkit.ui.interfaces.CallBack
import com.pkt.emarkit.utils.preferences.PrefsSessionManagement

class ProfileActivity : BaseActivity() {

    lateinit var binding:ActivityProfileBinding
    lateinit var menu: Menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile)

        session = PrefsSessionManagement(context)

        var image = session.getProfileImg()
        var name = session.getUserName()
        var mobile_no = session.getMobileNo()
        var email_id = session.getEmailId()
        var address = session.getAddress()

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
        binding.imageView.setOnClickListener {
            onBackPressed()
        }

        var menuItemId = R.id.profile1
        binding.navigationView.setCheckedItem(menuItemId)

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


        binding.userName.text = name
        binding.mobileNo.text = mobile_no
        binding.emailId.text = email_id
        binding.address.text = address

        Log.e("priyanka","user_img = $image")
        Log.e("priyanka","mobile_no = $mobile_no")
        Log.e("priyanka","address = $address")

        if(image != null){
            Glide.with(this).load(image).into(binding.userImg)
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
}