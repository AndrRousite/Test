package com.letion.test

import android.content.Context
import android.text.TextUtils
import android.util.SparseArray
import cn.hutool.core.thread.ThreadUtil
import com.letion.green_dao.DaoManager
import com.letion.green_dao.model.IMessage
import com.letion.green_dao.util.RandomUtil
import com.letion.test.util.AES
import com.xuhao.android.common.basic.bean.OriginalData
import com.xuhao.android.common.interfacies.client.msg.ISendable
import com.xuhao.android.libsocket.sdk.OkSocket
import com.xuhao.android.libsocket.sdk.client.ConnectionInfo
import com.xuhao.android.libsocket.sdk.client.OkSocketOptions
import com.xuhao.android.libsocket.sdk.client.action.ISocketActionListener
import com.xuhao.android.libsocket.sdk.client.action.SocketActionAdapter
import com.xuhao.android.libsocket.sdk.client.connection.IConnectionManager
import java.lang.Exception
import java.nio.charset.Charset
import java.util.*

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/13 0013
 */
object SocketManager {

    var sessionId = 64214439902
    private val from = "1"
    private val to = "0"

    var manager: IConnectionManager? = null
        get() = field
    private var listener: ISocketActionListener? = null

    private val sparseArray: LinkedList<ISocketActionListener> = LinkedList()

    fun onCreate() {
        val info = ConnectionInfo("104.238.184.237", 8080)
        manager = OkSocket.open(info)

        listener = object : SocketActionAdapter() {
            override fun onSocketConnectionSuccess(context: Context?, info: ConnectionInfo?, action: String?) {
                super.onSocketConnectionSuccess(context, info, action)
                assemble("$action：连接成功")
                sparseArray.forEach { it.onSocketConnectionSuccess(context, info, action) }
            }

            override fun onSocketConnectionFailed(context: Context?, info: ConnectionInfo?, action: String?, e: Exception?) {
                super.onSocketConnectionFailed(context, info, action, e)
                assemble("$action：连接错误")
                sparseArray.forEach { it.onSocketConnectionFailed(context, info, action, e) }
            }

            override fun onSocketDisconnection(context: Context?, info: ConnectionInfo?, action: String?, e: Exception?) {
                super.onSocketDisconnection(context, info, action, e)
                assemble("$action：断开连接")
                sparseArray.forEach { it.onSocketDisconnection(context, info, action, e) }
            }

            override fun onSocketReadResponse(context: Context?, info: ConnectionInfo?, action: String?, data: OriginalData?) {
                super.onSocketReadResponse(context, info, action, data)
                //assemble(data = AES.decode(data?.bodyBytes.toString()))
                assembleRead(data = AES.decrypt(data?.bodyBytes?.toString(Charset.defaultCharset())))
                sparseArray.forEach { it.onSocketReadResponse(context, info, action, data) }
            }

            override fun onSocketWriteResponse(context: Context?, info: ConnectionInfo?, action: String?, data: ISendable?) {
                super.onSocketWriteResponse(context, info, action, data)
                //assemble(data = AES.decode(data.toString()))
                sparseArray.forEach { it.onSocketWriteResponse(context, info, action, data) }
            }
        }

        manager!!.registerReceiver(listener)

        manager!!.option(OkSocketOptions.Builder()
                .setReadPackageBytes(1024)
                .setWritePackageBytes(1024)
                .build())

        manager!!.connect()
    }

    fun addISocketActionListener(listener: ISocketActionListener) {
        sparseArray.add(listener)
    }

    fun removeISocketActionListener(listener: ISocketActionListener) {
        sparseArray.remove(listener)
    }

    /**
     * 默认处理
     */
    fun assemble(data: String) {
        println(data)
    }

    fun assembleRead(data: String) {
        ThreadUtil.execute({
            DaoManager.getInstance().insertMessage(RandomUtil.number18().toLong(), Date().time, 0,
                    sessionId, from,
                    to, 0, 1, data, null, 0, 0)
        })
    }

    fun onDestroy() {
        manager?.disconnect()
        manager?.unRegisterReceiver(listener)
    }


}