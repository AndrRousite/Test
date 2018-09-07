package com.letion.java_server.util

import kotlin.experimental.and

object Log {
    @JvmStatic
    fun i(msg: String) {
        println(msg)
    }

    fun e(msg: String) {
        System.err.println(msg)
    }

    fun bytes(prefix: String, data: ByteArray?) {
        var debugSendBytes = ""
        if (data != null) {
            for (i in data.indices) {
                var tempHexStr = Integer.toHexString((data[i] and 0xff.toByte()).toInt()) + " "
                tempHexStr = if (tempHexStr.length == 2) "0$tempHexStr" else tempHexStr
                debugSendBytes += tempHexStr
            }
        }
        i(prefix + debugSendBytes)
    }

}
