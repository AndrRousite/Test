package com.letion.test.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.widget.ImageView
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.letion.green_dao.inputs.OnMenuItemClickListener
import com.letion.green_dao.inputs.airpanel.Util
import com.letion.green_dao.messages.MsgListAdapter
import com.letion.green_dao.util.ImageLoader
import com.letion.test.R
import com.letion.test.bean.TestMessage
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class ChatActivity : AppCompatActivity(), ChatView {

    var chatPresenter: ChatPresenter? = null
    private var list: MutableList<TestMessage>? = null
    private var msgListAdapter: MsgListAdapter<TestMessage>? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatPresenter = ChatPresenter()
        chatPresenter?.view = this
        chatPresenter?.sessionId = intent.getLongExtra("id", -1)
        chatPresenter?.onCreate()

        airPanelLayout.setup {
            Util.hideKeyboard(chat_view.chatInputView.getmChatInput())
        }

        list = ArrayList()

        chat_view.initModule()


        msgListAdapter = MsgListAdapter("0", object : ImageLoader {
            override fun loadAvatarImage(avatarImageView: ImageView, string: String) {
                if (string.contains("R.drawable")) {
                    val resId = resources.getIdentifier(string.replace("R.drawable.", ""),
                            "drawable", packageName)

                    avatarImageView.setImageResource(resId)
                } else {
                    Glide.with(this@ChatActivity)
                            .load(string)
                            .apply(RequestOptions().placeholder(R.drawable.aurora_headicon_default))
                            .into(avatarImageView)
                }
            }

            override fun loadImage(imageView: ImageView?, string: String?) {
            }

            override fun loadVideo(imageCover: ImageView?, uri: String?) {
            }
        })

        chat_view.msgList.setAdapter(msgListAdapter)

        chat_view.ptrLayout?.setOnRefreshListener {
            chatPresenter?.loadNextPage()
        }

        chat_view.setMenuClickListener(object : OnMenuItemClickListener {
            override fun switchToEmojiMode() {
                if (airPanelLayout.isOpen) {
                    Util.showKeyboard(chat_view.chatInputView.getmChatInput())
                } else {
                    airPanelLayout.openPanel()
                }
            }

            override fun onSendTextMessage(input: CharSequence?): Boolean {
                return chatPresenter!!.sendMessage(input)
            }
        })

        chat_view?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (airPanelLayout.isOpen) {
                    airPanelLayout.closePanel()
                }
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
            }
            false
        }

        chat_view?.msgList?.setOnTouchListener { v, event ->
            chat_view?.msgList?.smoothScrollToPosition(0)
            false
        }

        chatPresenter?.loadNextPage()
    }


    override fun onBackPressed() {
        if (airPanelLayout.isOpen) {
            airPanelLayout.closePanel()
            return
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        chatPresenter?.onDestroy()
    }

    override fun notifyTitle(title: String?) {
        setTitle(title)
    }

    override fun notifyData(msgId: String?, content: TestMessage) {
        runOnUiThread {
            msgListAdapter?.updateOrAddMessage(msgId ?: "", content, true)
        }
    }

    override fun notifyData(content: List<TestMessage>?) {
        runOnUiThread({
            chat_view.ptrLayout?.isRefreshing = false
            if (content != null && content.isNotEmpty()) {
                msgListAdapter?.addToEnd(content)

            } else
                ToastUtils.showShort("更有更多数据了")
        })
    }

    override fun scrollToPosition(position: Int) {
        runOnUiThread({
            chat_view?.msgList?.smoothScrollToPosition(position)
        })
    }
}
