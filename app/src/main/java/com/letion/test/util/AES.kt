package com.letion.test.util

import android.util.Base64
import com.blankj.utilcode.util.EncodeUtils
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/13 0013
 */
class AES {
    companion object {
        private const val key: String = "k8Kdf83bHvP19Ng5"
        private const val iv: String = "0298523660155212"
        private const val mode: String = "AES/CBC/PKCS5Padding"
        /**
         * AES 加密
         */
        fun encode(data: String): String {
            val secretKey: SecretKey = SecretKeySpec(key.toByteArray(), "AES")
            val cipher: Cipher = Cipher.getInstance(mode)

            val ivParameterSpec = IvParameterSpec(iv.toByteArray())

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)

//            return EncodeUtils.base64Encode2String(cipher.doFinal(data.toByteArray()))
            return Base64.encodeToString(cipher.doFinal(data.toByteArray()), Base64.DEFAULT)
        }

        /**
         * AES 解密
         */
        fun decode(data: String): String {
            val secretKey: SecretKey = SecretKeySpec(key.toByteArray(), "AES")
            val cipher: Cipher = Cipher.getInstance(mode)

            val ivParameterSpec = IvParameterSpec(iv.toByteArray())

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)

//            return String(cipher.doFinal(EncodeUtils.base64Decode(data.toByteArray())))
            return String(cipher.doFinal(Base64.decode(data.toByteArray(), Base64.DEFAULT)))
        }
    }
}