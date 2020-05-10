package com.up.share.jsbridgekotlin.util

import android.os.Build
import android.os.Looper
import android.os.SystemClock
import android.text.TextUtils
import android.webkit.WebView
import com.google.gson.Gson
import com.up.share.jsbridgekotlin.BridgeCallback
import com.up.share.jsbridgekotlin.bean.JSRequest
import java.net.URLEncoder

/**
 *
 * @author barry
 * @date 2020-05-05
 * @function
 */


class BridgeHelper(
    webview: WebView,
    messge: ArrayList<JSRequest>,
    callbackid: HashMap<String, BridgeCallback>
) {
    val messges =messge
    val callbackids = callbackid
    val mWebview = webview

    val mGson = Gson()
    var mUniqueId = 0


    fun sendToWeb(data: Any) {
        callHandler(null,data,null)
    }
    /**
     *  保存message到消息队列
     */
    fun callHandler(name: String?, data: Any?, callback: BridgeCallback?) {
        if (data !is String && mGson == null) return

        val jsRequest = JSRequest()

        if (data != null) jsRequest.data = if (data is String) data else mGson.toJson(data)

        if (callback != null) {
            var callbackId = String.format(
                CALLBACK_ID_FORMAT,
                "" + ++mUniqueId + (UNDERLINE_STR + SystemClock.currentThreadTimeMillis())
            )
            callbackids.put(callbackId, callback)
            jsRequest.callbackid = callbackId
        }
        if (!TextUtils.isEmpty(name)) {
            jsRequest.name = name
        }

        queueMessage(jsRequest)

    }


    private fun queueMessage(jsRequest: JSRequest) {

        if (messges == null||messges.size==0) {
            messges.add(jsRequest)
        } else {
            dispatchMessage(jsRequest)
        }

    }

     fun dispatchMessage(message: JSRequest) {
        if (mGson == null) {
            return
        }
        var messageJson = mGson.toJson(message)
        //escape special characters for json string  为json字符串转义特殊字符
        messageJson = messageJson.replace("(\\\\)([^utrn])".toRegex(), "\\\\\\\\$1$2")
        messageJson = messageJson.replace("(?<=[^\\\\])(\")".toRegex(), "\\\\\"")
        messageJson = messageJson.replace("(?<=[^\\\\])(\')".toRegex(), "\\\\\'")
        messageJson = messageJson.replace("%7B".toRegex(), URLEncoder.encode("%7B"))
        messageJson = messageJson.replace("%7D".toRegex(), URLEncoder.encode("%7D"))
        messageJson = messageJson.replace("%22".toRegex(), URLEncoder.encode("%22"))
        val javascriptCommand = String.format(JS_HANDLE_MESSAGE_FROM_JAVA, messageJson)
        // 必须要找主线程才会将数据传递出去 --- 划重点
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && javascriptCommand.length >= URL_MAX_CHARACTER_NUM) {
                mWebview.evaluateJavascript(javascriptCommand, null)
            } else {
                mWebview.loadUrl(javascriptCommand)
            }
        }
    }

}