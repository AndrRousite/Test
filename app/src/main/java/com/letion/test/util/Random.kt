package com.letion.test.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/13 0013
 */
object Random {
    @SuppressLint("SimpleDateFormat")
    fun number18(): String {
        val arg: Int = ((Math.random() * 10 + 1) * 1000).toInt()
        val date = SimpleDateFormat("yyyyMMddHHMMSS").format(Date())
        return arg.toString() + date
    }
}