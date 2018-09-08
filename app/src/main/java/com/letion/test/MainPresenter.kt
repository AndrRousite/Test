package com.letion.test

import android.content.Context
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.ToastUtils
import com.letion.green_dao.model.IMessage
import com.xuhao.android.common.basic.bean.OriginalData
import com.xuhao.android.common.interfacies.client.msg.ISendable
import com.xuhao.android.libsocket.sdk.OkSocket
import com.xuhao.android.libsocket.sdk.client.ConnectionInfo
import com.xuhao.android.libsocket.sdk.client.OkSocketOptions
import com.xuhao.android.libsocket.sdk.client.action.ISocketActionListener
import com.xuhao.android.libsocket.sdk.client.action.SocketActionAdapter
import com.xuhao.android.libsocket.sdk.client.connection.IConnectionManager
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import java.text.SimpleDateFormat
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
 * @date 2018/9/8 0008
 */
class MainPresenter {
    var mainView: com.letion.test.MainView? = null
        set(value) {
            field = value
        }

    private var manager: IConnectionManager? = null
    private var listener: ISocketActionListener? = null

    fun onCreate() {
        val info = ConnectionInfo("192.168.4.141", 7799)
        manager = OkSocket.open(info)

        listener = object : SocketActionAdapter() {
            override fun onSocketConnectionSuccess(context: Context?, info: ConnectionInfo?, action: String?) {
                super.onSocketConnectionSuccess(context, info, action)
                assemble("$action：连接成功")
            }

            override fun onSocketConnectionFailed(context: Context?, info: ConnectionInfo?, action: String?, e: Exception?) {
                super.onSocketConnectionFailed(context, info, action, e)
                assemble("$action：连接错误")
            }

            override fun onSocketDisconnection(context: Context?, info: ConnectionInfo?, action: String?, e: Exception?) {
                super.onSocketDisconnection(context, info, action, e)
                assemble("$action：断开连接")
            }

            override fun onSocketReadResponse(context: Context?, info: ConnectionInfo?, action: String?, data: OriginalData?) {
                super.onSocketReadResponse(context, info, action, data)
                assemble(data = decode(data?.bodyBytes.toString()))
            }

            override fun onSocketWriteResponse(context: Context?, info: ConnectionInfo?, action: String?, data: ISendable?) {
                super.onSocketWriteResponse(context, info, action, data)
                assemble(data = decode(data.toString()))
            }
        }

        manager!!.registerReceiver(listener)

        manager!!.option(OkSocketOptions.Builder()
                .setReadPackageBytes(1024)
                .setWritePackageBytes(1024)
                .build())

        manager!!.connect()
    }

    fun onDestory() {
        mainView = null
        manager?.disconnect()
        manager?.unRegisterReceiver(listener)
    }

    fun sendMessage(s: CharSequence?): Boolean {
        manager?.send(TestSendData(s.toString()))
        val message = TestMessage(s.toString(), IMessage.MessageType.SEND_TEXT.ordinal)
        message.user = TestUser("1", "Ironman", "R.drawable.ironman")
        message.time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        message.msgStatus = IMessage.MessageStatus.SEND_SUCCEED
//        mAdapter.addToStart(message, true)
        mainView?.notifyData(message)
        return true
    }

    fun loadNextPage() {
        ToastUtils.showShort("没有更多了")
    }

    private fun assemble(data: String) {
        val message = TestMessage(data, IMessage.MessageType.RECEIVE_TEXT.ordinal)
        message.user = TestUser("0", "DeadPool", "R.drawable.deadpool")
        message.time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        message.msgStatus = IMessage.MessageStatus.RECEIVE_SUCCEED
//        mAdapter.addToStart(message, true)
        mainView?.notifyData(message)
    }

    class TestSendData(var arg: String) : ISendable {

        override fun parse(): ByteArray {
            val body = arg.toByteArray(Charset.defaultCharset())
            val bb = ByteBuffer.allocate(4 + body.size)
            bb.order(ByteOrder.BIG_ENDIAN)
            bb.putInt(body.size)
            bb.put(body)
            return bb.array()
        }

        init {
            arg = encode("{\"type\":\"subscribe\",\"data\":{\"name\":\"" + arg + "\"}}")
        }

        override fun toString(): String {
            return arg
        }
    }

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

            return EncodeUtils.base64Encode2String(cipher.doFinal(data.toByteArray()))
        }

        /**
         * AES 解密
         */
        fun decode(data: String): String {
            val secretKey: SecretKey = SecretKeySpec(key.toByteArray(), "AES")
            val cipher: Cipher = Cipher.getInstance(mode)

            val ivParameterSpec = IvParameterSpec(iv.toByteArray())

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)

            return String(cipher.doFinal(EncodeUtils.base64Decode(data.toByteArray())))
        }
    }
}