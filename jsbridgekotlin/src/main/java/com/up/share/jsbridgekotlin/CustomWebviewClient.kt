package com.up.share.jsbridgekotlin

import android.webkit.WebView
import android.webkit.WebViewClient
import com.up.share.jsbridgekotlin.util.JAVA_SCRIPT
import com.up.share.jsbridgekotlin.util.webViewLoadLocalJs

/**
 *
 * @author barry
 * @date 2020-05-06
 * @function
 */

class CustomWebviewClient:WebViewClient {


     var mListener: WebViewClientCallback?=null

    constructor (listener: WebViewClientCallback) {
        mListener = listener
    }
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
       webViewLoadLocalJs(view, JAVA_SCRIPT)
        mListener!!.onloadPageFinished()
    }
}


 interface WebViewClientCallback {
    fun onloadPageFinished()
}