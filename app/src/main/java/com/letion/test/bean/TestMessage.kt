package com.letion.test.bean

import com.letion.green_dao.model.IMessage
import com.letion.green_dao.model.IUser
import java.util.*

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/8 0008
 */
class TestMessage(var content: String, val msgType: Int) : IMessage {
    var id: Long = 0L

    var time: String? = null
        set(value) {
            field = value
        }
    var user: IUser? = null
        set(value) {
            field = value
        }
    var mediaPath: String? = null
        set(value) {
            field = value
        }
    var durationTime: Long = 0L
        set(value) {
            field = value
        }
    var progressString: String? = null
        set(value) {
            field = value
        }
    var msgStatus: IMessage.MessageStatus = IMessage.MessageStatus.CREATED
        set(value) {
            field = value
        }

    init {
        id = UUID.randomUUID().leastSignificantBits
    }

    override fun getMsgId(): String {
        return id.toString()
    }

    override fun getFromUser(): IUser? {
        return user
    }

    override fun getTimeString(): String? {
        return time
    }

    override fun getType(): Int {
        return msgType
    }

    override fun getMessageStatus(): IMessage.MessageStatus {
        return msgStatus
    }

    override fun getText(): String {
        return content
    }

    override fun getMediaFilePath(): String? {
        return mediaPath
    }

    override fun getDuration(): Long {
        return durationTime
    }

    override fun getProgress(): String? {
        return progressString
    }

    override fun getExtras(): HashMap<String, String>? {
        return null
    }
}