package com.letion.test.chat

import android.content.Context
import android.text.TextUtils
import cn.hutool.core.thread.ThreadUtil
import com.letion.green_dao.DaoManager
import com.letion.green_dao.dao.Contact
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
import java.util.*

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/13 0013
 */
class ChatPresenter : BasePresenter<ChatView>() {
    var pageIndex: Int = 0
    var noMore: Boolean = false
    private var listener: ISocketActionListener? = null
    var sessionId = 64214439902

    private var contact: Contact? = null
    private val from = "0"

    override fun onCreate() {
        super.onCreate()

        listener = object : SocketActionAdapter() {
            override fun onSocketConnectionSuccess(context: Context?, info: ConnectionInfo?, action: String?) {
                super.onSocketConnectionSuccess(context, info, action)
                view?.notifyTitle(contact?.name)
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
                receiverMessage(data = AES.decrypt(data?.bodyBytes?.toString(Charset.defaultCharset())))
            }

        }
        SocketManager.addISocketActionListener(listener as SocketActionAdapter)

        ThreadUtil.execute {
            contact = DaoManager.getInstance().searchContactById(sessionId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketManager.removeISocketActionListener(listener as SocketActionAdapter)
    }


    fun sendMessage(s: CharSequence?): Boolean {
        SocketManager.manager?.send(TestSendData(s.toString()))
        val message = TestMessage(s.toString(), IMessage.MessageType.SEND_TEXT.ordinal)
        message.id = RandomUtil.number18().toLong()
        message.user = TestUser(from, "DeadPool", "R.drawable.deadpool")
        message.time = com.letion.test.util.Date.getTimestampString(Date())

        ThreadUtil.execute({

            ThreadUtil.sleep(500)
            val msgId = DaoManager.getInstance().insertMessage(message.id, Date().time, 0,
                    sessionId, from, contact?.userId, 0, 0, s.toString(), null, 0, 0)

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

    fun loadNextPage() {
        if (noMore) {
            view?.notifyData(null)
            return
        }
        ThreadUtil.execute {
            val messages = DaoManager.getInstance().searchMessage(sessionId, pageIndex, 10)

            this.noMore = null == messages || messages.isEmpty()

            val list: MutableList<TestMessage> = ArrayList()

            for (i in messages.indices) {
                val it = messages[i]

                val lastIt = if (i - 1 > 0 && i - 1 < messages.size) messages[i - 1] else null

                if (it.msgType == 0) {
                    val message = TestMessage(it.content, if (it.from == from) IMessage.MessageType
                            .SEND_TEXT
                            .ordinal else IMessage.MessageType
                            .RECEIVE_TEXT
                            .ordinal)


                    if (it.from == from) {
                        message.user = TestUser(from, "DeadPool", "R.drawable.deadpool")

                        message.time = if (lastIt != null && com.letion.test.util.Date
                                        .isCloseEnough(it.msgTime, lastIt.msgTime)) null else
                            com.letion.test.util.Date
                                    .getTimestampString(Date(it.msgTime))

                        message.msgStatus = if (it.status == 0) IMessage.MessageStatus.SEND_SUCCEED else IMessage
                                .MessageStatus.SEND_FAILED
                    } else {
                        message.user = TestUser(contact?.userId, contact?.name, contact?.avatar)

                        message.time = if (lastIt != null && com.letion.test.util.Date
                                        .isCloseEnough(it.msgTime, lastIt.msgTime)) null else
                            com.letion.test.util.Date
                                    .getTimestampString(Date(it.msgTime))

                        message.msgStatus = if (it.status == 0) IMessage.MessageStatus.RECEIVE_SUCCEED else IMessage
                                .MessageStatus.RECEIVE_FAILED
                    }

                    list.add(0, message)

                }

            }

            view?.notifyData(list)

            if (pageIndex == 0) {
                view?.scrollToPosition(0)
            }

            pageIndex++

        }
    }

    fun receiverMessage(data: String) {
        val message = TestMessage(data, IMessage.MessageType.RECEIVE_TEXT.ordinal)
        message.user = TestUser(contact?.userId, contact?.name, "R.drawable.deadpool")
        message.time = com.letion.test.util.Date.getTimestampString(Date())
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
            arg = AES.encrypt("{\"type\":\"subscribe\",\"data\":{\"name\":\"$arg\"}}")
        }

        override fun toString(): String {
            return arg
        }
    }

}