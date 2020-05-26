package com.hearing.myhandler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hearing.myhandler.custom.MyHandler
import com.hearing.myhandler.custom.MyLooper
import com.hearing.myhandler.custom.MyMessage

class MainActivity : AppCompatActivity() {

    private var handler: MyHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread {
            MyLooper.prepare()
            handler = object : MyHandler() {
                override fun handleMessage(msg: MyMessage) {
                    msg.print()
                    if (msg.what == 3) {
                        MyLooper.myLooper()?.quit()
                    }
                }
            }
            MyLooper.loop()
        }.start()

        Thread {
            sleep(2000)
            handler?.sendEmptyMessage(1)
        }.start()

        Thread {
            sleep(3000)
            val msg = MyMessage.obtain()
            msg.what = 2
            msg.obj = "hearing"
            handler?.sendMessage(msg)
        }.start()

        Thread {
            sleep(4000)
            val msg = MyMessage()
            msg.what = 3
            msg.obj = "hearing-1"
            handler?.sendMessage(msg)
        }.start()

        Thread {
            sleep(5000)
            val msg = MyMessage.obtain()
            msg.what = 4
            msg.obj = "hearing-2"
            handler?.sendMessage(msg)
        }.start()
    }

    private fun sleep(time: Long) {
        Thread.sleep(time)
    }
}
