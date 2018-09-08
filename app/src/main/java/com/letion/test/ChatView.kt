package com.letion.test

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.letion.green_dao.inputs.ChatInputView
import com.letion.green_dao.inputs.OnMenuItemClickListener
import com.letion.green_dao.messages.MessageList
import com.letion.green_dao.messages.ptr.PullToRefreshLayout

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/8 0008
 */
class ChatView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout
(context, attrs, defStyleAttr) {
    lateinit var msgList: MessageList
    var ptrLayout: PullToRefreshLayout? = null
        get() = field

    lateinit var chatInputView: ChatInputView

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        // ...
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    @SuppressLint("NewApi")
    fun initModule() {
        msgList = findViewById(R.id.msg_list)
        ptrLayout = findViewById(R.id.pull_to_refresh_layout)
        chatInputView = findViewById(R.id.chat_input)

        msgList.setHasTransientState(true)
    }

    fun setMenuClickListener(listener: OnMenuItemClickListener) {
        chatInputView.setMenuClickListener(listener)
    }
}