package com.up.share.jsbridgekotlin

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView
import com.google.gson.Gson
import com.up.share.jsbridgekotlin.bean.JSRequest
import com.up.share.jsbridgekotlin.util.BridgeHelper


/**
 *
 * @author barry
 * @date 2020-05-04
 * @function
 */

class BridgeWebview : WebView, WebViewClientCallback {


    val messges = arrayListOf<JSRequest>()
    val callbackids = hashMapOf<String, BridgeCallback>()
    val bridge = BridgeHelper(this, messges, callbackids)
    lateinit var mGson:Gson
    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {

        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyle: Int
    ) : super(context, attrs, defStyle) {

        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        clearCache(true)
        settings.useWideViewPort = true
//		webView.getSettings().setLoadWithOverviewMode(true);
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.javaScriptEnabled = true
//        mContent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        settings.javaScriptCanOpenWindowsAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            setWebContentsDebuggingEnabled(true)
        }
        val mClient = CustomWebviewClient(this)

       super.setWebViewClient(mClient)
    }

    fun callHandler(name: String, data: Any, callback: BridgeCallback) {
        bridge.callHandler(name, data, callback)
    }

    private fun sendToWeb(data: Any) {
        bridge.sendToWeb(data)
    }


    override fun destroy() {
        super.destroy()
        callbackids.clear()
    }

    override fun onloadPageFinished() {
        if (messges != null) {
            messges.forEach { bridge.dispatchMessage(it) }
            messges.clear()
        }
    }

    fun setGson(gson: Gson) {
        mGson= gson
     }

}