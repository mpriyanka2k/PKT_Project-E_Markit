package com.pkt.emarkit.utils.helper

import android.util.Base64
import android.util.Log
import java.util.Base64.getEncoder
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionHandler {

    companion object{
        private var instance: EncryptionHandler? = null
        fun getInstance(): EncryptionHandler {
            if (instance == null) {
                instance = EncryptionHandler()
            }
            return instance!!
        }
    }


//    var key = "secret0key000000"
    var key: String = "primarykeytech@gmail.com"


    val key1 = "0123456789abcdef0123456789abcdef"
    val iv = "0123456789abcdef"




    fun getAESEncrypted(value: String):String?{
        try {

            val skeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
            val encrypted = cipher.doFinal(value.toByteArray())
            return Base64.encodeToString(encrypted, Base64.NO_WRAP)
        } catch (e: Exception) {
           Log.d("EncryptionHandler","Exception1 =$e")
        }
        return null
    }

    fun getAESDecrypted(ciphertext:String,initVector:String):String?{
        try {

            val iv = IvParameterSpec(initVector.toByteArray(charset("UTF-8")))
            val skeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            val original = cipher.doFinal(Base64.decode(ciphertext, Base64.NO_WRAP))
            Log.d("EncryptionHandler","original = $original")
            return String(original)
        } catch (e: Exception) {
            Log.d("EncryptionHandler","Exception2 =$e")
        }
        return null
    }


    fun Encryption(text:String): String? {

        try {
            val keySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
            val ivSpec = IvParameterSpec(iv.toByteArray(charset("UTF-8")))
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encryptedBytes = cipher.doFinal(text.toByteArray())
            return Base64.encodeToString(encryptedBytes,Base64.NO_WRAP)
        }
        catch (e:Exception){
            return null
        }

    }

    fun Decryption(encrypted:String?): String? {
        try {
            val keySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
            val ivSpec = IvParameterSpec(iv.toByteArray(charset("UTF-8")))
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec)
            var encryptedByte = Base64.decode(encrypted,Base64.NO_WRAP)
            var original = cipher.doFinal(encryptedByte)
            return String(original)
        }
        catch (e:Exception){
            return null
        }


    }



}