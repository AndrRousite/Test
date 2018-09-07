package com.letion.java_server.util

import java.net.InetSocketAddress
import java.net.Socket

object PortCheck {

    @Throws(Exception::class)
    private fun bindPort(host: String, port: Int) {
        val s = Socket()
        s.bind(InetSocketAddress(host, port))
        s.close()
    }

    @JvmStatic
    fun isPortAvailable(port: Int): Boolean {
        try {
            bindPort("0.0.0.0", port)
            bindPort("127.0.0.1", port)
            return true
        } catch (e: Exception) {
            return false

        }

    }
}
