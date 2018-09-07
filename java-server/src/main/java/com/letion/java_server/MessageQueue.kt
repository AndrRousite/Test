package com.letion.java_server

import com.letion.java_server.bean.MsgBean
import java.util.concurrent.LinkedBlockingDeque

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/7 0007
 */
class MessageQueue {
    private val queue = LinkedBlockingDeque<MsgBean>()


    class InstanceHolder {
        companion object {
            val INSTANCE: MessageQueue = MessageQueue()
        }
    }

    companion object {
        @JvmStatic
        fun getInstace() : MessageQueue {
            return InstanceHolder.INSTANCE
        }
    }

    public fun offer(msgBean: MsgBean): MessageQueue {
        queue.offer(msgBean)
        return this
    }

    public fun take(): MsgBean? {
        try {
            return queue.take()

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    public fun clear() {
        queue.clear()
    }
}