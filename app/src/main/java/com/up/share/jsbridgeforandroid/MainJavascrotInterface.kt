package com.up.share.jsbridgeforandroid

import android.util.Log
import android.webkit.JavascriptInterface
import com.up.share.jsbridgekotlin.BridgeCallback
import com.up.share.jsbridgekotlin.BridgeWebview

/**
 *
 * @author barry
 * @date 2020-05-10
 * @function
 */

class MainJavascrotInterface(callbacks: HashMap<String, BridgeCallback>,webView: BridgeWebview) :BridgeWebview.BaseJavascriptInterface (callbacks){

     var mWebView=webView


    override  fun send(data: String): String {
        return "it is default response"
    }


    @JavascriptInterface
    fun submitFromWeb(data: String, callbackId: String) {
        Log.d("chromium data", data + ", callbackId: " + callbackId + " " + Thread.currentThread().name
        )
    }
}