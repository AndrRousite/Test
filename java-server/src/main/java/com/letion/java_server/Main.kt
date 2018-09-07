package com.letion.java_server

import com.letion.java_server.util.Log
import com.letion.java_server.util.PortCheck

import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.UnknownHostException

object Main {

    val DEFAULT_PORT = 8080

    private var port: Int = 0

    private var serverSocket: ServerSocket? = null

    @JvmStatic
    fun main(args: Array<String>) {
        preparePort(args)
        prepareSocketServer(args)
    }

    private fun prepareSocketServer(args: Array<String>?) {
        try {
            serverSocket = ServerSocket(port)
            serverSocket!!.reuseAddress = true
            Log.i("server is waiting for client...")
            while (true) {
                val socket = serverSocket!!.accept()
                Log.i("client accept:" + socket.inetAddress.hostAddress)
                var isHex = false
                if (args != null) {
                    for (str in args) {
                        if ("-hex".equals(str, ignoreCase = true)) {
                            isHex = true
                        }
                    }
                }
                MessageDispathcer(socket, isHex)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun preparePort(args: Array<String>?) {
        if (args == null || args.size == 0) {
            port = DEFAULT_PORT
        } else {
            try {
                for (i in args.indices) {
                    if ("-port".equals(args[i], ignoreCase = true)) {
                        if (i < args.size - 1) {
                            port = Integer.parseInt(args[i + 1])
                        }
                    }
                }
            } catch (e: Exception) {
                port = DEFAULT_PORT
            }

        }
        while (!PortCheck.isPortAvailable(port) && port < 65535) {
            port++
        }
        if (port == 65535 && !PortCheck.isPortAvailable(port)) {
            throw IllegalArgumentException(port.toString() + " port is not available")
        }
        try {
            val ia = InetAddress.getLocalHost()
            Log.i("IP: " + ia.hostAddress + " /Port is " + port)
        } catch (e: UnknownHostException) {
            Log.i("IP: 127.0.0.1 /PORT: $port")
        }

    }
}
