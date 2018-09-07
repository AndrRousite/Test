package com.letion.java_server.bean

import java.io.Serializable

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/7 0007
 */
data class MsgBean(val toWho: String, val fromWho: String, val byte: ByteArray = ByteArray(0)) : Serializable