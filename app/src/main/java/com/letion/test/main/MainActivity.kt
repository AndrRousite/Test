package com.letion.test.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.letion.green_dao.dao.Conversation
import com.letion.test.R
import com.letion.test.chat.ChatActivity
import kotlinx.android.synthetic.main.activity_main.*

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

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> startActivity(Intent(this@MainActivity, ChatActivity::class.java)) }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter?.onDestroy()
    }

    override fun notifyStatus(code: Int, content: String) {
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

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val viewHolder: ViewHolder
            val itemView: View?
            if (convertView == null) {
                itemView = inflater?.inflate(android.R.layout.simple_list_item_2, parent, false)
                viewHolder = ViewHolder()
                viewHolder.text1 = itemView?.findViewById(android.R.id.text1)
                viewHolder.text2 = itemView?.findViewById(android.R.id.text2)
                itemView?.tag = viewHolder
            } else {
                itemView = convertView
                viewHolder = itemView.tag as ViewHolder
            }

            val data = getItem(position)

            viewHolder.text1?.text = data.name

            viewHolder.text2?.text = data.avatar

            return itemView
        }

        override fun getItem(position: Int): Conversation {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return list.size
        }
    }

    inner class ViewHolder {
        var text1: TextView? = null
        var text2: TextView? = null
    }
}
