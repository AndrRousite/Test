package com.letion.test.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/14 0014
 */
object Date {

    fun isCloseEnough(var0: Long, var2: Long): Boolean {
        var var4 = var0 - var2
        if (var4 < 0L) {
            var4 = -var4
        }

        return var4 < 30000L
    }

    fun getTimestampString(var0: Date): String {
        var var1: String? = null
        val var2 = Locale.getDefault().language
        val var3 = var2.startsWith("zh")
        val var4 = var0.time
        if (isSameDay(var4)) {
            if (var3) {
                var1 = "aa hh:mm"
            } else {
                var1 = "hh:mm aa"
            }
        } else if (isYesterday(var4)) {
            if (!var3) {
                return "Yesterday " + SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(var0)
            }

            var1 = "昨天aa hh:mm"
        } else if (var3) {
            var1 = "M月d日aa hh:mm"
        } else {
            var1 = "MMM dd hh:mm aa"
        }

        return if (var3) SimpleDateFormat(var1, Locale.CHINESE).format(var0) else SimpleDateFormat(var1, Locale.ENGLISH).format(var0)
    }

    private fun isSameDay(var0: Long): Boolean {
        val var2 = getTodayStartAndEndTime()
        return var0 > var2.startTime && var0 < var2.endTime
    }

    private fun isYesterday(var0: Long): Boolean {
        val var2 = getYesterdayStartAndEndTime()
        return var0 > var2.startTime && var0 < var2.endTime
    }

    fun getYesterdayStartAndEndTime(): TimeInfo {
        val var0 = Calendar.getInstance()
        var0.add(5, -1)
        var0.set(11, 0)
        var0.set(12, 0)
        var0.set(13, 0)
        var0.set(14, 0)
        val var1 = var0.time
        val var2 = var1.time
        val var4 = Calendar.getInstance()
        var4.add(5, -1)
        var4.set(11, 23)
        var4.set(12, 59)
        var4.set(13, 59)
        var4.set(14, 999)
        val var5 = var4.time
        val var6 = var5.time
        val var8 = TimeInfo()
        var8.startTime = var2
        var8.endTime = var6
        return var8
    }

    fun getTodayStartAndEndTime(): TimeInfo {
        val var0 = Calendar.getInstance()
        var0.set(11, 0)
        var0.set(12, 0)
        var0.set(13, 0)
        var0.set(14, 0)
        val var1 = var0.time
        val var2 = var1.time
        val var4 = Calendar.getInstance()
        var4.set(11, 23)
        var4.set(12, 59)
        var4.set(13, 59)
        var4.set(14, 999)
        val var5 = var4.time
        val var6 = var5.time
        val var8 = TimeInfo()
        var8.startTime = var2
        var8.endTime = var6
        return var8
    }


    class TimeInfo {
        var startTime: Long = 0
        var endTime: Long = 0
    }
}