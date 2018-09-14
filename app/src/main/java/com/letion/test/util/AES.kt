package com.letion.test.util

import android.util.Base64
import cn.hutool.core.convert.Convert.toByte
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


        //加密
        fun encrypt(inpute: String?): String {
            //创建cipher对象
            val cipher = Cipher.getInstance("AES")
            //初始化cipher
            //通过秘钥工厂生产秘钥
            val keySpec = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            //加密、解密
            val encrypt = cipher.doFinal(inpute?.toByteArray())
            return Base64.encodeToString(encrypt, Base64.URL_SAFE)
        }

        //解密
        fun decrypt(inpute: String?): String {
            //创建cipher对象
            val cipher = Cipher.getInstance("AES")
            //初始化cipher
            //通过秘钥工厂生产秘钥
            val keySpec = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            //加密、解密
            val encrypt = cipher.doFinal(Base64.decode(inpute, Base64.URL_SAFE))
            return String(encrypt)
        }

        /**
         * AES 加密
         */
        fun encode(data: String): String {
            val secretKey: SecretKey = SecretKeySpec(key.toByteArray(), "AES")
            val cipher: Cipher = Cipher.getInstance(mode)

            val ivParameterSpec = IvParameterSpec(iv.toByteArray())

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)

//            return EncodeUtils.base64Encode2String(cipher.doFinal(data.toByteArray()))
//            return Base64.encodeToString(cipher.doFinal(data.toByteArray()), Base64.DEFAULT)
            return byte2HexString(cipher.doFinal(data.toByteArray()))
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
//            return String(cipher.doFinal(Base64.decode(data.toByteArray(), Base64.DEFAULT)))
            return String(cipher.doFinal(hexString2Byte(data)))
        }

        /**
         * 数组转换成十六进制字符串
         * @param bArray
         * @return HexString
         */
        fun byte2HexString(bArray: ByteArray): String {
            val sb = StringBuffer(bArray.size)
            var sTemp: String
            for (i in bArray.indices) {
                sTemp = Integer.toHexString(0xFF and bArray[i].toInt())
                if (sTemp.length < 2)
                    sb.append(0)
                sb.append(sTemp.toUpperCase())
            }

            return sb.toString()
        }

        fun hexString2Byte(hex: String): ByteArray {
            val len = hex.length / 2
            val result = ByteArray(len)
            val achar = hex.toCharArray()
            for (i in 0 until len) {
                val pos = i * 2
                result[i] = ("0123456789ABCDEF".indexOfFirst { it == achar[pos] } shl 4 or (
                        "0123456789ABCDEF").indexOfFirst { it == achar[pos + 1] }).toByte()
            }
            return result
        }
    }
}