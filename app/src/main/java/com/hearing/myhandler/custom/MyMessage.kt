package com.hearing.myhandler.custom

import android.util.Log

/**
 * @author liujiadong
 * @since 2020/5/26
 */
class MyMessage {
    var what = 0
    var obj: Any? = null
    var target: MyHandler? = null
    var next: MyMessage? = null
    var inUse = false

    companion object {
        private const val MAX_POOL_SIZE = 10
        private val sPoolSync = Any()
        private var sPool: MyMessage? = null
        private var sPoolSize = 0

        fun obtain(): MyMessage {
            synchronized(sPoolSync) {
                if (sPool != null) {
                    Log.d(Debug.TAG, "Reuse message: $sPool.")
                    val m = sPool
                    sPool = m?.next
                    m?.next = null
                    m?.inUse = false
                    sPoolSize--
                    return m ?: MyMessage()
                }
            }
            return MyMessage()
        }
    }

    fun recycle() {
        if (inUse) {
            throw IllegalStateException("This message cannot be recycled because it is still in use.")
        }
        recycleUnchecked()
    }

    fun recycleUnchecked() {
        inUse = true
        what = 0
        obj = null
        target = null

        synchronized(sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
                Log.d(Debug.TAG, "Add message: $this.")
                next = sPool
                sPool = this
                sPoolSize++
            }
        }
    }

    fun print() {
        synchronized(this) {
            Log.d(Debug.TAG, "\n-----------------message start--------------------")
            Log.d(
                Debug.TAG,
                "Message-$this: what = $what, obj = $obj, target = $target, inUse = $inUse."
            )
            Log.d(Debug.TAG, "-----------------message end----------------------")
        }
    }
}
