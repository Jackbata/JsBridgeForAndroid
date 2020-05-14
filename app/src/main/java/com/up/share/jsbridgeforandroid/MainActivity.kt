package com.up.share.jsbridgeforandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.up.share.jsbridgeforandroid.bean.SendData
import com.up.share.jsbridgekotlin.BridgeCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bridgeWebview.setGson(Gson())
        bridgeWebview.loadUrl("file:///android_asset/demo.html")
        bridgeWebview.addJavascriptInterface(MainJavascrotInterface(bridgeWebview.callbackids,bridgeWebview),"android")
        val sendData = SendData("测试", 12)


        button.setOnClickListener{
            bridgeWebview.callHandler("functionInJs",Gson().toJson(sendData), object : BridgeCallback{
                override fun callback(data: String) {
                    Log.d("AppCompatActivity",data)
                }
            })
        }
    }
}
