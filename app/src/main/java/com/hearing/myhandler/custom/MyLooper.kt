package com.hearing.myhandler.custom

import android.util.Log

/**
 * @author liujiadong
 * @since 2020/5/26
 */
class MyLooper {

    val mQueue = MyMessageQueue()

    companion object {
        private val sThreadLocal = ThreadLocal<MyLooper>()

        fun prepare() {
            require(sThreadLocal.get() == null) { "Only one Looper may be created per thread" }
            sThreadLocal.set(MyLooper())
        }

        fun loop() {
            val me = myLooper()
            requireNotNull(me) { "No Looper; MyLooper.prepare() wasn't called on this thread." }
            val queue = me.mQueue
            while (true) {
                val msg = queue.next() ?: return
                Log.d(Debug.TAG, "Begin to dispatch message: $msg")
                msg.target?.dispatchMessage(msg)
                msg.recycleUnchecked()
            }
        }

        fun myLooper(): MyLooper? {
            return sThreadLocal.get()
        }
    }

    fun quit() {
        mQueue.quit()
    }
}