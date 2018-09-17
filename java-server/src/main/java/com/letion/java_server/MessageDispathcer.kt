package com.letion.java_server

import com.letion.java_server.bean.MsgBean
import com.letion.java_server.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future


/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/7 0007
 */
class MessageDispathcer(socket: Socket, isHex: Boolean) {
    companion object {
        private val readThreadPool: ExecutorService = Executors.newCachedThreadPool()
        private val writeThreadPool: ExecutorService = Executors.newCachedThreadPool()
        private val outputStreamMap: ConcurrentHashMap<String, OutputStream> = ConcurrentHashMap()
    }

    var socket: Socket
    lateinit var inputStream: InputStream
    lateinit var outputStream: OutputStream
    lateinit var readFuture: Future<*>
    lateinit var writeFuture: Future<*>
    var remainingBuffer: ByteBuffer? = null
    var isHex: Boolean = false

    init {
        this.isHex = isHex
        this.socket = socket

        try {
            this.inputStream = socket.getInputStream()
            this.outputStream = socket.getOutputStream()
            outputStreamMap.put(socket.inetAddress.hostAddress, outputStream)

            // start io thread
            readFuture = readThreadPool.submit(Reader())
            writeFuture = writeThreadPool.submit(Writer())

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private inner class Reader : Runnable {
        override fun run() {
            try {
                while (!socket.isClosed) {
                    var totalBuf: ByteBuffer?
                    try {
                        val headBuf = ByteBuffer.allocate(4)
                        if (remainingBuffer != null) {
                            remainingBuffer!!.flip()
                            val length = Math.min(remainingBuffer!!.remaining(), 4)
                            headBuf.put(remainingBuffer!!.array(), 0, length)
                            if (length < 4) {
                                //there are no data left
                                remainingBuffer = null
                                for (i in 0 until 4 - length) {
                                    headBuf.put(inputStream.read() as Byte)
                                }
                            } else {
                                remainingBuffer!!.position(4)
                            }
                        } else {
                            for (i in 0 until headBuf.capacity()) {
                                headBuf.put(inputStream.read() as Byte)
                            }
                        }
                        headBuf.flip()
                        val bodyLength = headBuf.int
                        var bodyArray = ByteArray(0)
                        if (bodyLength > 0) {
                            if (bodyLength > 10 * 1024 * 1024) {
                                throw IllegalArgumentException("we can't read data bigger than " + 10 + "Mb")
                            }
                            val byteBuffer = ByteBuffer.allocate(bodyLength)
                            if (remainingBuffer != null) {
                                val bodyStartPosition = remainingBuffer!!.position()
                                val length = Math.min(remainingBuffer!!.remaining(), bodyLength)
                                byteBuffer.put(remainingBuffer!!.array(), bodyStartPosition, length)
                                remainingBuffer!!.position(bodyStartPosition + length)
                                if (length == bodyLength) {
                                    if (remainingBuffer!!.remaining() > 0) {//there are data left
                                        val temp = ByteBuffer.allocate(remainingBuffer!!.remaining())
                                        temp.put(remainingBuffer!!.array(), remainingBuffer!!.position(), remainingBuffer!!.remaining())
                                        remainingBuffer = temp
                                    } else {//there are no data left
                                        remainingBuffer = null
                                    }
                                    //cause this time data from remaining buffer not from channel.
                                    bodyArray = byteBuffer.array()
                                    totalBuf = ByteBuffer.allocate(4 + bodyArray.size)
                                    headBuf.flip()
                                    totalBuf!!.put(headBuf)
                                    totalBuf.put(bodyArray)
                                    totalBuf.flip()
                                    if (isHex) {
                                        Log.bytes("read from:" + socket.inetAddress.hostAddress + " data:", totalBuf.array())
                                    } else {
                                        Log.i("read from:" + socket.inetAddress.hostAddress + " data:"
                                                + String(totalBuf.array(), Charset.forName("utf-8")))
                                    }
                                    MessageQueue.getInstace().offer(MsgBean(socket.inetAddress.hostAddress, null!!, totalBuf.array()))
                                    return
                                } else {//there are no data left in buffer and some data pieces in channel
                                    remainingBuffer = null
                                }
                            }
                            readBodyFromChannel(byteBuffer)
                            bodyArray = byteBuffer.array()
                        } else if (bodyLength == 0) {
                            bodyArray = ByteArray(0)
                            if (remainingBuffer != null) {
                                //the body is empty so header remaining buf need set null
                                if (remainingBuffer!!.hasRemaining()) {
                                    val temp = ByteBuffer.allocate(remainingBuffer!!.remaining())
                                    temp.put(remainingBuffer!!.array(), remainingBuffer!!.position(), remainingBuffer!!.remaining())
                                    remainingBuffer = temp
                                } else {
                                    remainingBuffer = null
                                }
                            }
                        } else if (bodyLength < 0) {
                            throw IllegalArgumentException(
                                    "this socket input stream has some problem,wrong body length " + bodyLength
                                            + ",we'll disconnect")
                        }
                        totalBuf = ByteBuffer.allocate(4 + bodyArray.size)
                        headBuf.flip()
                        totalBuf!!.put(headBuf)
                        totalBuf.put(bodyArray)
                        totalBuf.flip()
                    } catch (e: Exception) {
                        throw e
                    }

                    //                        if (mIsHex) {
                    Log.bytes("read from:" + socket.inetAddress.hostAddress + " data:", totalBuf.array())
                    //                        } else {
                    //                            Log.i("read from:" + mSocket.getInetAddress().getHostAddress() + " data:"
                    //                                    + new String(totalBuf.array(), Charset.forName("utf-8")));
                    //                        }
                    MessageQueue.getInstace().offer(MsgBean(socket.inetAddress.hostAddress, null!!, totalBuf.array()))
                }
            } catch (e: Exception) {
                Log.e("read error: " + e.message)
            } finally {
                try {
                    outputStream.close()
                    inputStream.close()
                    socket.close()
                } catch (e: IOException) {
                }

                writeFuture.cancel(true)
                readFuture.cancel(true)
            }
        }
    }

    inner class Writer : Runnable {
        override fun run() {
            try {
                while (!socket.isClosed) {
                    val msgBean = MessageQueue.getInstace().take()

                    if (msgBean != null) {

                        val it = outputStreamMap.keySet(outputStream).iterator()
                        while (it.hasNext()) {
                            val key = it.next()
                            try {
                                val os = outputStreamMap[key]
                                os?.write(msgBean.byte)
                                os?.flush()
                            } catch (e: IOException) {
                                Log.e("ip:" + key + " " + e.message)
                                outputStreamMap.remove(key)
                            }

                        }

                        //                        if (mIsHex) {
                        Log.bytes("write from:" + msgBean.fromWho + " to all data:", msgBean.byte)
                        //                        } else {
                        //                            Log.i("write from:" + msgBean.getFromWho() + " to all data:"
                        //                                    + new String(msgBean.getBytes(), Charset.forName("utf-8")));
                        //                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("write error: " + e.message)
            } finally {
                outputStreamMap.remove(socket.inetAddress.hostAddress)
                try {
                    Log.e("write error:" + socket.inetAddress.hostAddress + " client is disconnect")
                    outputStream.close()
                    inputStream.close()
                    socket.close()
                } catch (e: IOException) {
                    Log.e(socket.inetAddress.hostAddress + "client is disconnect with exception")
                }

                writeFuture.cancel(true)
                readFuture.cancel(true)
            }
        }
    }

    @Throws(IOException::class)
    private fun readBodyFromChannel(byteBuffer: ByteBuffer) {
        while (byteBuffer.hasRemaining()) {
            try {
                val bufArray = ByteArray(100)
                val len = inputStream.read(bufArray)
                if (len < 0) {
                    break
                }
                val remaining = byteBuffer.remaining()
                if (len > remaining) {
                    byteBuffer.put(bufArray, 0, remaining)
                    remainingBuffer = ByteBuffer.allocate(len - remaining)
                    remainingBuffer!!.put(bufArray, remaining, len - remaining)
                } else {
                    byteBuffer.put(bufArray, 0, len)
                }
            } catch (e: Exception) {
                throw e
            }

        }
    }
}