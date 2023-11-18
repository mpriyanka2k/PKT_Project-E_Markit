package com.pkt.emarkit.ui.viewmodels

import android.app.Application
import android.util.Base64
import android.util.Log
import android.util.Patterns
import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import com.pkt.emarkit.api.RetrofitClientObj
import com.pkt.emarkit.api.api_models.LoginRequest
import com.pkt.emarkit.api.api_models.LoginResponse
import com.pkt.emarkit.ui.interfaces.LoginCallbackInterface
import com.pkt.emarkit.utils.helper.Decryption
import com.pkt.emarkit.utils.helper.Decryptor
import com.pkt.emarkit.utils.helper.EncryptionHandler
import com.pkt.emarkit.utils.preferences.PrefsSessionManagement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger


@Keep
class LoginViewmodel(application: Application):BaseViewmodel(application) {

    var email:String? = null
    var password:String? = null

    var isValid:Boolean = true
    var loading:MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var session = PrefsSessionManagement(context)
    var encryptionHandler = EncryptionHandler.getInstance()
    var decryptor:Decryptor = Decryptor()
    var decryption = Decryption()


    fun callLoginApi(email:String,password:String,callback:LoginCallbackInterface){
     isValid = true
     if (email.isNullOrEmpty()){
         Log.e("priyanka","email")
      callback.onFail("Please enter email")
      isValid = false
      return
     }
     if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
      callback.onFail("Please enter valid email")
      isValid = false
      return
     }
     if (password.isNullOrEmpty()){
      callback.onFail("Please enter password")
      isValid = false
      return
     }

     if (isValid){
      // call login api
         loading.value = true
      var loginRequest = LoginRequest()
         loginRequest.email = email!!
         loginRequest.password = password!!
         loginRequest.notEncrypted = true

         RetrofitClientObj(context).instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
             override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                 loading.value = false

                 if (response.isSuccessful){
                     if (response.body()?.status.equals("success")){
                         var data = response.body()!!.data

//                         var raw_data = "Hello Priyanka"
//                         var encrypted_data = encryptionHandler.Encryption(raw_data)
//                         var decrypted_data = encryptionHandler.Decryption(encrypted_data)
//
//                         Log.d("priyanka","encrypted_data = $encrypted_data")
//                         Log.d("priyanka","decrypted_data = $decrypted_data")
//
//                         val encryptedBytes: ByteArray = Base64.decode(data, Base64.DEFAULT)
//                         val string: String = String(encryptedBytes, Charsets.UTF_8)
//
//                         val jsonObject = JSONObject(string)
//                         var  ciphertext = jsonObject.getString("ciphertext")
//                         var  ivhexString = jsonObject.getString("iv")
//                         var  salthex = jsonObject.getString("salt")
//
//                         Log.d("Loginviewmodel","ciphertext = $ciphertext")
//                         Log.d("Loginviewmodel","ivhexString = $ivhexString")
//                         Log.d("Loginviewmodel","salthex = $salthex")
//
//                         var iv = hexToAscii(ivhexString)
//                         var salt = hexToAscii(ivhexString)
//
//                         Log.d("Loginviewmodel","iv = $iv")
//                         Log.d("Loginviewmodel","salt = $salt")

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                         val jsonObject = JSONObject(jsonString)
//                         var  ciphertext = jsonObject.getString("ciphertext")
//                         Log.d("Loginviewmodel","ciphertext = $ciphertext")
//
//                         var ivhexString = jsonObject.getString("iv")
//
////                         var ivString = hexToString(ivhexString)
////                         Log.d("LoginViewmodel","ivString =$ivString")
//                         val iv:IvParameterSpec = IvParameterSpec(ivhexString.toByteArray(charset("UTF-8")))
//                         Log.d("LoginViewmodel","ivhexString = $ivhexString")
//                         Log.d("LoginViewmodel","iv = $iv")
//
//                         var decryptedResponse = encryptionHandler.getAESDecrypted1(ciphertext,iv)
////                         Log.d("LoginViewmodel","decryptedResponse = $decryptedResponse")

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//                         try {
//                             val jsonObject = JSONObject(jsonString)
//                             ciphertext = jsonObject.getString("ciphertext")
//
//                             var ivhexString = jsonObject.getString("iv")
//
//                             Log.d("LoginViewmodel","ivhexString =$ivhexString")
//                             Log.d("LoginViewmodel","ivhexString_lenght =${ivhexString.length}")
//
//                             val byteArray: ByteArray = Hex.decodeHex(ivhexString.toCharArray())
//
//                             val iv_string: String = StringUtils.newStringUtf8(byteArray)
//                             Log.d("LoginViewmodel","iv_string = $iv_string")
//                             Log.d("LoginViewmodel","iv_string_length = ${iv_string.length}")
//
//                             Log.d("LoginViewmodel","ciphertext = $ciphertext")
//
//                             var decryptedResponse = encryptionHandler.getAESDecrypted(ciphertext,iv_string)
//                             Log.d("LoginViewmodel","decryptedResponse = $decryptedResponse")
//
//
//                         }
//                         catch (e:Exception){
//                           Log.d("LoginViewmodel","Exception = $e")
//                         }








//                         if (data != null && !(data.equals(""))){
//                           var decryptedResponse =encryptionHandler.getAESDecrypted(data,"")
//                             Log.d("LoginViewModel","decryptedResponse = $decryptedResponse")
//
//                             var logindata: Data? =Gson().fromJson(decryptedResponse,Data::class.java)
//                           Log.d("LoginViewModel","logindata = $logindata")
//                         }
//                         else{
//                             Log.d("LoginViewModel","Data is null")
//                         }


                         if (response.body() != null){
                             Log.d("priyanka","access_token = ${response.body()!!.access_token}")
                             session.createLoginSession(response.body()!!.access_token, response.body()!!.data!!.user_details!!.id,response.body()!!.data!!.user_details!!.name,response.body()!!.data!!.user_details!!.email,
                                 response.body()!!.data!!.user_details!!.mobile,
                                 response.body()!!.data!!.user_details!!.profile_image,
                                 response.body()!!.data!!.user_details!!.address)
                             callback.onSuccess()
                         }
                     }
                     else{
                         callback.onFail(response.body()!!.message)
                         Log.d("priyanka","demo1")
                     }

                 }
                 else{
                     Log.d("priyanka","demo2")
                     showToast("UnSuccessful Response")
                 }
             }
             override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                 callback.onApiFail(t)
                 Log.d("priyanka","demo3")
             }

         })
     }
    }

    private fun hexToAscii(hexString: String): String {

        val hexChars = hexString.chunked(2)
        val asciiChars = hexChars.map { it.toInt(16).toChar() }
        return asciiChars.joinToString("")

    }



}