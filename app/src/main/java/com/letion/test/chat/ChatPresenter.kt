package com.letion.test.chat

import android.content.Context
import android.text.TextUtils
import cn.hutool.core.thread.ThreadUtil
import com.letion.green_dao.DaoManager
import com.letion.green_dao.model.IMessage
import com.letion.green_dao.util.RandomUtil
import com.letion.test.SocketManager
import com.letion.test.base.BasePresenter
import com.letion.test.bean.TestMessage
import com.letion.test.bean.TestUser
import com.letion.test.util.AES
import com.xuhao.android.common.basic.bean.OriginalData
import com.xuhao.android.common.interfacies.client.msg.ISendable
import com.xuhao.android.libsocket.sdk.client.ConnectionInfo
import com.xuhao.android.libsocket.sdk.client.action.ISocketActionListener
import com.xuhao.android.libsocket.sdk.client.action.SocketActionAdapter
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/13 0013
 */
class ChatPresenter : BasePresenter<ChatView>() {
    private var listener: ISocketActionListener? = null
    private val sessionId = 1L
    private val from = "0"
    private val to = "1"

    override fun onCreate() {
        super.onCreate()

        listener = object : SocketActionAdapter() {
            override fun onSocketConnectionSuccess(context: Context?, info: ConnectionInfo?, action: String?) {
                super.onSocketConnectionSuccess(context, info, action)
                view?.notifyTitle("DeadPool")
            }

            override fun onSocketConnectionFailed(context: Context?, info: ConnectionInfo?, action: String?, e: Exception?) {
                super.onSocketConnectionFailed(context, info, action, e)
                view?.notifyTitle("连接失败")
            }

            override fun onSocketDisconnection(context: Context?, info: ConnectionInfo?, action: String?, e: Exception?) {
                super.onSocketDisconnection(context, info, action, e)
                view?.notifyTitle("未连接")
            }

            override fun onSocketReadResponse(context: Context?, info: ConnectionInfo?, action: String?, data: OriginalData?) {
                super.onSocketReadResponse(context, info, action, data)
                receiverMessage(data = AES.decode(data?.bodyBytes.toString()))
            }

            override fun onSocketWriteResponse(context: Context?, info: ConnectionInfo?, action: String?, data: ISendable?) {
                super.onSocketWriteResponse(context, info, action, data)
                //receiverMessage(data = AES.decode(data.toString()))
            }
        }
        SocketManager.addISocketActionListener(listener as SocketActionAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketManager.removeISocketActionListener(listener as SocketActionAdapter)
    }


    fun sendMessage(s: CharSequence?): Boolean {
        SocketManager.manager?.send(TestSendData(s.toString()))
        val message = TestMessage(s.toString(), IMessage.MessageType.SEND_TEXT.ordinal)
        message.id = RandomUtil.number18().toLong()
        message.user = TestUser("1", "Ironman", "R.drawable.ironman")
        message.time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        ThreadUtil.execute({

            ThreadUtil.sleep(500)
            val msgId = DaoManager.getInstance().insertMessage(message.id, Date().time, 0,
                    sessionId, from,
                    to, 0, 0, s.toString(), null, 0, 0)

            if (!TextUtils.isEmpty(msgId)) {
                message.msgStatus = IMessage.MessageStatus.SEND_SUCCEED
            } else {
                message.msgStatus = IMessage.MessageStatus.SEND_FAILED
            }
            view?.notifyData(msgId, message)
        })

        view?.notifyData(null, message)
        return true
    }

    fun loadNextPage(page: Int) {
        ThreadUtil.execute {
            val messages = DaoManager.getInstance().searchMessage(sessionId, page, 10)

            val conversation = DaoManager.getInstance().searchConversationById(sessionId)

            val list: MutableList<TestMessage> = ArrayList()

            messages.forEach({

                if (it.msgType == 0) {
                    val message = TestMessage(it.content, if (it.from.equals(from)) IMessage.MessageType
                            .SEND_TEXT
                            .ordinal else IMessage.MessageType
                            .RECEIVE_TEXT
                            .ordinal)


                    if (it.from.equals(from)) {
                        message.user = TestUser(from, "Ironman", "R.drawable.ironman")

                        message.time = SimpleDateFormat.getInstance().format(Date(it.msgTime))

                        message.msgStatus = IMessage.MessageStatus.SEND_SUCCEED
                    } else {
                        message.user = TestUser(conversation.userId, conversation.name, conversation.avatar)

                        message.time = SimpleDateFormat.getInstance().format(Date(it.msgTime))

                        message.msgStatus = IMessage.MessageStatus.RECEIVE_SUCCEED
                    }

                    list.add(message)

                }

            })

            view?.notifyData(list)

        }
    }

    fun receiverMessage(data: String) {
        val message = TestMessage(data, IMessage.MessageType.RECEIVE_TEXT.ordinal)
        message.user = TestUser("0", "DeadPool", "R.drawable.deadpool")
        message.time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        message.msgStatus = IMessage.MessageStatus.RECEIVE_SUCCEED
        view?.notifyData(null, message)
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
            arg = AES.encode("{\"type\":\"subscribe\",\"data\":{\"name\":\"" + arg +
                    "\"}}")
        }

        override fun toString(): String {
            return arg
        }
    }

}