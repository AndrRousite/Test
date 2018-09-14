package com.letion.test.main

import android.content.Context
import cn.hutool.core.thread.ThreadUtil
import com.letion.green_dao.DaoManager
import com.letion.test.SocketManager
import com.letion.test.base.BasePresenter
import com.letion.test.util.AES
import com.xuhao.android.common.basic.bean.OriginalData
import com.xuhao.android.common.interfacies.client.msg.ISendable
import com.xuhao.android.libsocket.sdk.client.ConnectionInfo
import com.xuhao.android.libsocket.sdk.client.action.ISocketActionListener
import com.xuhao.android.libsocket.sdk.client.action.SocketActionAdapter
import java.lang.Exception

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/8 0008
 */
class MainPresenter : BasePresenter<MainView>() {
    private var listener: ISocketActionListener? = null

    override fun onCreate() {
        super.onCreate()

        listener = object : SocketActionAdapter() {
            override fun onSocketConnectionSuccess(context: Context?, info: ConnectionInfo?, action: String?) {
                super.onSocketConnectionSuccess(context, info, action)
                receiverMessage(100, "$action：连接成功")
            }

            override fun onSocketConnectionFailed(context: Context?, info: ConnectionInfo?, action: String?, e: Exception?) {
                super.onSocketConnectionFailed(context, info, action, e)
                receiverMessage(101, "$action：连接错误")
            }

            override fun onSocketDisconnection(context: Context?, info: ConnectionInfo?, action: String?, e: Exception?) {
                super.onSocketDisconnection(context, info, action, e)
                receiverMessage(102, "$action：断开连接")
            }

            override fun onSocketReadResponse(context: Context?, info: ConnectionInfo?, action: String?, data: OriginalData?) {
                super.onSocketReadResponse(context, info, action, data)
                receiverMessage(1, null)
            }

            override fun onSocketWriteResponse(context: Context?, info: ConnectionInfo?, action: String?, data: ISendable?) {
                super.onSocketWriteResponse(context, info, action, data)
                receiverMessage(1, null)
            }
        }
        SocketManager.addISocketActionListener(listener as SocketActionAdapter)
    }

    fun loadConversation() {
        ThreadUtil.execute({
            view?.notifyConversation(DaoManager.getInstance().search())
        })
    }

    fun addOrUpdateConversation() {
        ThreadUtil.execute({
            val l = DaoManager.getInstance().addOrUpdateConversation("0", "DeadPool", "R.drawable.deadpool",
                    "13207962457")
            val l2 = DaoManager.getInstance().addOrUpdateConversation("1", "Ironman", "R" +
                    ".drawable.ironman",
                    "15014298677")
            if (l >= 0) {
                loadConversation()
            }
        })
    }

    private fun receiverMessage(code: Int, data: String?) {
        view?.notifyStatus(code, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketManager.removeISocketActionListener(listener as SocketActionAdapter)
    }

}