package com.letion.test

import android.app.Application
import com.xuhao.android.libsocket.sdk.OkSocket

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/7 0007
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        //在主进程初始化一次,多进程时需要区分主进程.
        OkSocket.initialize(this, true)
    }
}