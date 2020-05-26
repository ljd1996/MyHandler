package com.hearing.myhandler.custom

/**
 * @author liujiadong
 * @since 2020/5/26
 */
open class MyHandler {

    private val looper: MyLooper = MyLooper.myLooper() ?: throw RuntimeException(
        "Can't create handler inside thread ${Thread.currentThread()} that has not called MyLooper.prepare()"
    )
    private val queue: MyMessageQueue = looper.mQueue

    fun sendMessage(msg: MyMessage): Boolean {
        msg.target = this
        return queue.enqueueMessage(msg)
    }

    fun sendEmptyMessage(what: Int): Boolean {
        val msg = MyMessage.obtain()
        msg.what = what
        return sendMessage(msg)
    }

    fun dispatchMessage(msg: MyMessage) {
        handleMessage(msg)
    }

    open fun handleMessage(msg: MyMessage) {

    }
}