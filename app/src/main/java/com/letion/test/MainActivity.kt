package com.letion.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.letion.green_dao.inputs.OnMenuItemClickListener
import com.letion.green_dao.inputs.airpanel.Util
import com.letion.green_dao.messages.MsgListAdapter
import com.letion.green_dao.util.ImageLoader
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), MainView {
    var mainPresenter: MainPresenter? = null
    private var list: MutableList<TestMessage>? = null
    private var msgListAdapter: MsgListAdapter<TestMessage>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainPresenter = MainPresenter()
        mainPresenter?.mainView = this
        mainPresenter?.onCreate()

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
                    Glide.with(this@MainActivity)
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

        chat_view.ptrLayout?.setPtrHandler {
            mainPresenter?.loadNextPage()
        }

        chat_view.setMenuClickListener(object : OnMenuItemClickListener {
            override fun switchToEmojiMode() {
                if (airPanelLayout.isOpen) {
                    Util.hideKeyboard(chat_view.chatInputView.getmChatInput())
                } else {
                    airPanelLayout.openPanel()
                }
            }

            override fun onSendTextMessage(input: CharSequence?): Boolean {
                return mainPresenter!!.sendMessage(input)
            }
        })

        chat_view.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (airPanelLayout.isOpen) {
                    airPanelLayout.closePanel()
                }
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
            }
            false
        }

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
        mainPresenter?.onDestory()
    }

    override fun notifyData(content: TestMessage) {
        runOnUiThread {
            list!!.add(0, content)
            msgListAdapter?.addToStart(content, true)
        }
    }

}
