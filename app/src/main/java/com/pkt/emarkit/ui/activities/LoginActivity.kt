package com.pkt.emarkit.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.pkt.emarkit.R
import com.pkt.emarkit.databinding.ActivityLoginBinding
import com.pkt.emarkit.ui.interfaces.LoginCallbackInterface
import com.pkt.emarkit.ui.viewmodels.LoginViewmodel

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var loginViewmodel :LoginViewmodel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        binding.executePendingBindings()

        loginViewmodel = ViewModelProviders.of(this).get(LoginViewmodel::class.java)

        initObserver()
        binding.loginBtn.setOnClickListener {
            Log.e("priyanka","emailEditText = ${binding.emailEditText.editText!!.text}")
          loginViewmodel.callLoginApi(binding.emailEditText.editText!!.text.toString(),binding.editPassword.editText!!.text.toString(),object : LoginCallbackInterface {
              override fun onSuccess() {
                  Log.d("LoginActivity","goto HomeActivity")
                  var intent = Intent(this@LoginActivity,HomeActivity::class.java)
                  startActivity(intent)
                  finish()
              }
              override fun onFail(message: String) {
                  showShortToast(message)
              }
              override fun onApiFail(message: Throwable) {
                 RetrofitExceptionHandling(null,message)
                  dismissProgress()
              }
              override fun onApiUnsuccessResponse(responseCode: Int) {
                  RetrofitUnSuccessfulResponse(responseCode)
                  dismissProgress()
              }
          })
        }
    }

    private fun initObserver() {
        loginViewmodel.loading.observe(this, Observer {
            if (it){
              showProgress()
            }
            else{
                dismissProgress()
            }
        })
    }
}