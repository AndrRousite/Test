package com.letion.java_server.bean

import java.io.Serializable
import java.util.*

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/7 0007
 */
data class MsgBean(val toWho: String, val fromWho: String?, val byte: ByteArray?) :
        Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MsgBean

        if (toWho != other.toWho) return false
        if (fromWho != other.fromWho) return false
        if (!Arrays.equals(byte, other.byte)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = toWho.hashCode()
        result = 31 * result + (fromWho?.hashCode() ?: 0)
        result = 31 * result + (byte?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }
}