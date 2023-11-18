package com.pkt.emarkit.utils.helper

import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.spec.KeySpec
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class Decryptor {

    fun decrypt(encryptedString: String?): String? {
        val key = "primarykeytech@gmail.com"
        var decrypted: String?
        try {
            val decodedBytes: ByteArray = Base64.decode(encryptedString, Base64.DEFAULT)
            val json = String(decodedBytes, StandardCharsets.UTF_8)

            // Extract salt and iv from JSON
            val saltHex = extractValueFromJson(json, "salt")
            val ivHex = extractValueFromJson(json, "iv")
            val salt = hexStringToByteArray(saltHex!!)
            val iv = hexStringToByteArray(ivHex!!)

            // Derive key using PBKDF2
            val keyFactory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
            val keySpec: KeySpec = PBEKeySpec(key.toCharArray(), salt, 999, 128)
            val hashKeyBytes: ByteArray = keyFactory.generateSecret(keySpec).getEncoded()
            val secretKey = SecretKeySpec(hashKeyBytes, "AES")

            // Decrypt ciphertext
//            val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val cipher: Cipher = Cipher.getInstance("AES-256-CBC")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
            val cipherText: ByteArray = Base64.decode(extractValueFromJson(json, "ciphertext"), Base64.DEFAULT)
            val decryptedBytes: ByteArray = cipher.doFinal(cipherText)

            // Clean up decrypted string
            decrypted = String(decryptedBytes, StandardCharsets.UTF_8)
            decrypted = decrypted.replace("[\\x00-\\x1F\\x7F]".toRegex(), "")
        } catch (e: Exception) {
            Log.d("Decryptor","Exception = $e")
            decrypted = null
        }
        return decrypted
    }

    private fun extractValueFromJson(json: String, key: String): String? {
//        val pattern = "\"$key\":\"(.*?)\""
//        return Pattern.compile(pattern)
//            .matcher(json)
//            .results()
//            .map { matchResult -> matchResult.group(1) }
//            .findFirst()
//            .orElse(null)

        val pattern = "\"$key\":\"(.*?)\""
        val matcher = Pattern.compile(pattern)
            .matcher(json)

        val result = if (matcher.find()) {
            matcher.group(1)
        } else {
            null
        }

        return result
    }

    private fun hexStringToByteArray(hexString: String): ByteArray {
        val length = hexString.length
        val bytes = ByteArray(length / 2)
        var i = 0
        while (i < length) {
            bytes[i / 2] = ((hexString[i].digitToIntOrNull(16) ?: -1 shl 4)
            + hexString[i + 1].digitToIntOrNull(16)!! ?: -1).toByte()
            i += 2
        }
        return bytes
    }
}