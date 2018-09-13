package com.letion.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import cn.hutool.system.HostInfo
import cn.hutool.system.OsInfo
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val list: ArrayList<String> = ArrayList()

        Thread(Runnable {
            Thread.sleep(1000)
            try {
                val hostInfo = HostInfo()
                val osInfo = OsInfo()
                list.add(String.format("name: %s\taddress: %s", hostInfo.name, hostInfo.address))
                list.add(String.format("name: %s\tversion: %s", osInfo.name, osInfo.version))
            } catch (e: NoClassDefFoundError) {
                e.printStackTrace()
            }
            runOnUiThread({
                val adapter: BaseAdapter = listView.adapter as BaseAdapter
                adapter.notifyDataSetChanged()
            })
        }).start()

        listView.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
    }


}
