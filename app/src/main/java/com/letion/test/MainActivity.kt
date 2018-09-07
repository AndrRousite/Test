package com.letion.test

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import com.blankj.utilcode.util.EncodeUtils
import com.xuhao.android.common.basic.bean.OriginalData
import com.xuhao.android.common.interfacies.client.msg.ISendable
import com.xuhao.android.libsocket.sdk.OkSocket
import com.xuhao.android.libsocket.sdk.client.ConnectionInfo
import com.xuhao.android.libsocket.sdk.client.OkSocketOptions
import com.xuhao.android.libsocket.sdk.client.action.ISocketActionListener
import com.xuhao.android.libsocket.sdk.client.action.SocketActionAdapter
import com.xuhao.android.libsocket.sdk.client.connection.IConnectionManager
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {

    private var manager: IConnectionManager? = null
    private var listener: ISocketActionListener? = null
    private var arrayAdapter: ArrayAdapter<String>? = null
    private var list: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list = ArrayList()
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

        recyclerView.adapter = arrayAdapter

        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                manager!!.send(TestSendData(v.text.toString()))
            }
            false
        }

        val info = ConnectionInfo("192.168.4.141", 7799)

        manager = OkSocket.open(info)

        listener = object : SocketActionAdapter() {
            override fun onSocketConnectionSuccess(context: Context?, info: ConnectionInfo?, action: String?) {
                super.onSocketConnectionSuccess(context, info, action)
                notifyData("$action：连接成功")
            }

            override fun onSocketConnectionFailed(context: Context?, info: ConnectionInfo?, action: String?, e: Exception?) {
                super.onSocketConnectionFailed(context, info, action, e)
                notifyData("$action：连接错误")
            }

            override fun onSocketDisconnection(context: Context?, info: ConnectionInfo?, action: String?, e: Exception?) {
                super.onSocketDisconnection(context, info, action, e)
                notifyData("$action：断开连接")
            }

            override fun onSocketReadResponse(context: Context?, info: ConnectionInfo?, action: String?, data: OriginalData?) {
                super.onSocketReadResponse(context, info, action, data)
                notifyData(data = decode(data?.bodyBytes.toString()))
            }

            override fun onSocketWriteResponse(context: Context?, info: ConnectionInfo?, action: String?, data: ISendable?) {
                super.onSocketWriteResponse(context, info, action, data)
                notifyData(data = decode(data.toString()))
            }
        }

        manager!!.registerReceiver(listener)

        manager!!.option(OkSocketOptions.Builder()
                .setReadPackageBytes(1024)
                .setWritePackageBytes(1024)
                .build())

        manager!!.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        manager!!.disconnect()
        manager!!.unRegisterReceiver(listener)
    }

    fun notifyData(data: String) {
        runOnUiThread {
            list!!.add(0, data)
            arrayAdapter!!.notifyDataSetChanged()
        }
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
