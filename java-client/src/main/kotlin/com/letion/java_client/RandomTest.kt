package com.letion.java_client

import java.text.NumberFormat
import java.util.*

/**
 *
 *
 *
 * @author wuqi
 * @describe ...
 * @date 2018/9/11 0011
 */
class RandomTest {

    private fun generate(size: Int): Array<String?> {
        val array = arrayOfNulls<String>(size)

        for (i in 0 until size) {
            val numberFormat = NumberFormat.getNumberInstance()
            numberFormat.minimumIntegerDigits = 2
            array[i] = numberFormat.format((i + 1).toLong())
        }
        return array
    }

    private fun random(length: Int, arg: Array<String?>): String? {
        val random = Random().nextInt(length + 1)

        val result = arg[random]

        arg[random] = arg[length]


        return result
    }

    fun exce(): String {
        val blue = generate(15)
        val red = generate(33)

        //println(Arrays.toString(blue))

        //println(Arrays.toString(red))

        var length1 = blue.size
        var length2 = red.size

        val sb = StringBuilder()

        for (i in 0..5) {
            sb.append(random(--length2, red)).append(if (i == 5) "\t-\t-\t" else ",")
        }

        for (i in 0..0) {
            sb.append(random(--length1, blue))
        }

        return sb.toString()
    }

}
