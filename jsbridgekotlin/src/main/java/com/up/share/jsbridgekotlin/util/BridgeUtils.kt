package com.up.share.jsbridgekotlin.util

import android.content.Context
import android.webkit.WebView
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 *
 * @author barry
 * @date 2020-05-06
 * @function
 */

const val CALLBACK_ID_FORMAT = "JAVA_CB_%s"
const val UNDERLINE_STR = "_"
const val JS_HANDLE_MESSAGE_FROM_JAVA =
    "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');"
const val URL_MAX_CHARACTER_NUM = 2097152
val JAVA_SCRIPT = "WebViewJavascriptBridge.js"

fun webViewLoadLocalJs(view: WebView?, path: String) {
    val jsContent = assetFile2Str(view!!.context, path)
    view.loadUrl("javascript:" + jsContent!!)
}

/**
 * 解析assets文件夹里面的代码,去除注释,取可执行的代码
 * @param c context
 * @param urlStr 路径
 * @return 可执行代码
 */
fun assetFile2Str(c: Context, urlStr: String): String? {
    var `in`: InputStream? = null
    try {
        `in` = c.assets.open(urlStr)
        val bufferedReader = BufferedReader(InputStreamReader(`in`))
        var line: String? = null
        val sb = StringBuilder()
        do {
            line = bufferedReader.readLine()
            if (line != null && !line.matches("^\\s*\\/\\/.*".toRegex())) { // 去除注释
                sb.append(line)
            }
        } while (line != null)

        bufferedReader.close()
        `in`.close()

        return sb.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (`in` != null) {
            try {
                `in`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    return null
}