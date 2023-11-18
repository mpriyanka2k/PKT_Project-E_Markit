package com.pkt.emarkit.utils.helper

import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Decryption {

//    fun decrypte(encryptedBytes: ByteArray){
//        val key = "..." // The encryption key used in the backend
//
//        val secretKeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
//        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
//        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(ivBytes))
//
//        val decryptedBytes: ByteArray = cipher.doFinal(encryptedBytes)
//        val decryptedData = String(decryptedBytes, StandardCharsets.UTF_8)
//    }
}