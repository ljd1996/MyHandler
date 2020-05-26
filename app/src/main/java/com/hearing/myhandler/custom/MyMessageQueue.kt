package com.hearing.myhandler.custom

import android.text.TextUtils
import android.util.Log

/**
 * @author liujiadong
 * @since 2020/5/26
 */
class MyMessageQueue {
    private var head: MyMessage? = null
    private var tail: MyMessage? = null
    private var isQuit = false

    fun enqueueMessage(msg: MyMessage): Boolean {
        requireNotNull(msg.target) { "Message must have a target." }
        require(!msg.inUse) { "This message is already in use: $msg." }

        synchronized(this) {
            if (isQuit) {
                Log.d(Debug.TAG, "Send message on a quit message queue.")
                msg.recycle()
                return false
            }
            msg.inUse = true
            if (head == null || tail == null) {
                head = msg
                tail = msg
            } else {
                tail?.next = msg
                tail = msg
            }
            tail?.next = null
            print("enqueueMessage:")
        }
        return true
    }

    fun next(): MyMessage? {
        if (isQuit) {
            Log.d(Debug.TAG, "MessageQueue is quit.")
            return null
        }
        while (true) {
            synchronized(this) {
                val msg = head
                if (msg != null) {
                    Log.d(Debug.TAG, "Return message: $msg")
                    head = msg.next
                    msg.next = null
                    msg.inUse = true
                    print("next:")
                    return msg
                }
                if (isQuit) {
                    return null
                }
            }
        }
    }

    fun quit() {
        synchronized(this) {
            if (isQuit) {
                return
            }
            isQuit = true
            removeAllMessages()
            print("quit:")
        }
    }

    private fun removeAllMessages() {
        var p = head
        while (p != null) {
            val n = p.next
            p.recycleUnchecked()
            p = n
        }
        head = null
        tail = null
    }

    private fun print(s: String?) {
        if (!TextUtils.isEmpty(s)) {
            Log.d(Debug.TAG, s)
        }
        Log.d(Debug.TAG, "-----------------message queue start--------------------")
        var p = head
        var index = 0
        while (p != null) {
            val n = p.next
            Log.d(Debug.TAG, "${index++}: $p; ")
            p = n
        }
        Log.d(Debug.TAG, "-----------------message queue end----------------------")
    }
}