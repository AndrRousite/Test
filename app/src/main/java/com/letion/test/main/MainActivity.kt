package com.letion.test.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonParseException
import com.letion.green_dao.dao.Conversation
import com.letion.test.R
import com.letion.test.SocketManager
import com.letion.test.chat.ChatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), MainView {

    var mainPresenter: MainPresenter? = null
    var testAdapter: TestAdapter? = null
    lateinit var mList: MutableList<Conversation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainPresenter = MainPresenter()
        mainPresenter?.view = this
        mainPresenter?.onCreate()


        mList = ArrayList()
        testAdapter = TestAdapter(this, mList)
        listView.adapter = testAdapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position,
                                                                         id ->
            run {
                SocketManager.sessionId = id
                val intent = Intent(this@MainActivity, ChatActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter?.onDestroy()
    }

    override fun notifyStatus(code: Int, content: String?) {
        tvStatus.visibility = if (code == 1 || code == 100) View.GONE else View.VISIBLE
        tvStatus.text = content

        if (code == 100) {
            mainPresenter?.addOrUpdateConversation()
        }
    }

    override fun notifyConversation(data: List<Conversation>) {
        runOnUiThread({
            mList.clear()
            mList.addAll(data)
            testAdapter?.notifyDataSetChanged()
        })
    }

    inner class TestAdapter(val context: Context, val list: List<Conversation>) : BaseAdapter() {
        var inflater: LayoutInflater? = null

        init {
            inflater = LayoutInflater.from(context)
        }

        @SuppressLint("SetTextI18n")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val viewHolder: ViewHolder
            val itemView: View?
            if (convertView == null) {
                itemView = inflater?.inflate(R.layout.item_conversation, parent, false)
                viewHolder = ViewHolder()
                viewHolder.ivAvatar = itemView?.findViewById(R.id.ivAvatar)
                viewHolder.tvName = itemView?.findViewById(R.id.tvName)
                viewHolder.tvContent = itemView?.findViewById(R.id.tvContent)
                viewHolder.tvCount = itemView?.findViewById(R.id.tvCount)
                itemView?.tag = viewHolder
            } else {
                itemView = convertView
                viewHolder = itemView.tag as ViewHolder
            }

            val item = getItem(position)
            val data = getItem(position).extras

            var name: String? = null
            var avatar: String? = null

            try {
                val jsonObject = JSONObject(data)
                name = jsonObject.getString("username")
                avatar = jsonObject.getString("avatar")
            } catch (e: JsonParseException) {
                e.printStackTrace()
            }

            if (avatar != null && avatar.contains("R.drawable")) {
                val resId = resources.getIdentifier(avatar.replace("R.drawable.", ""),
                        "drawable", packageName)

                viewHolder.ivAvatar?.setImageResource(resId)
            } else if (avatar != null && viewHolder.ivAvatar != null) {
                Glide.with(this@MainActivity)
                        .load(avatar)
                        .apply(RequestOptions().placeholder(R.drawable.aurora_headicon_default))
                        .into(viewHolder.ivAvatar!!)
            }


            viewHolder.tvName?.text = name

            return itemView
        }

        override fun getItem(position: Int): Conversation {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return getItem(position).id
        }

        override fun getCount(): Int {
            return list.size
        }
    }

    inner class ViewHolder {
        var ivAvatar: ImageView? = null
        var tvName: TextView? = null
        var tvContent: TextView? = null
        var tvCount: TextView? = null
    }
}
