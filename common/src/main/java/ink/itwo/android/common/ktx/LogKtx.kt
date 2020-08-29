package ink.itwo.android.common.ktx

import android.os.Looper
import android.util.Log
import ink.itwo.android.common.CommonUtil
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/** Created by wang on 2020/5/6. */


fun Any?.log(tag: String = CommonUtil.TAG, write: Boolean? = false) {
    LogKtx.print(tag, this.toString(), write ?: false)
}

object LogKtx {
    private const val SUFFIX = ".kt"
    private const val MAX_LENGTH = 4000
    private const val STACK_TRACE_INDEX = 5
    private const val JSON_INDENT = 4
    private val LINE_SEPARATOR = System.getProperty("line.separator")
    fun log(s: String) {
        print(CommonUtil.TAG, s, write = false)
    }

    fun print(tag: String, message: String?, write: Boolean) {
        if (!CommonUtil.DEBUG) return
        val stackTrace = Thread.currentThread().stackTrace
        val targetElement = stackTrace[STACK_TRACE_INDEX]
        targetElement?.let { print(it, tag, message ?: "", write) }
    }

    private fun print(element: StackTraceElement, tagLog: String, msg: String, write: Boolean) {
        val contents: Array<String> = wrapperContent(element, tagLog, msg)
        val tag = contents[0]
        val message = contents[1]
        val headString = contents[2]
        printDefault(tag, headString + message, write)
    }

    private fun wrapperContent(element: StackTraceElement, TAG: String, msg: String): Array<String> {
        var className = element.className
        if (className.contains("$")) {
            className = className?.split("$")?.firstOrNull()
        }
        className = (className?.split(".")?.lastOrNull() ?: "") + SUFFIX
        val methodName = element.methodName
        var lineNumber = element.lineNumber
        if (lineNumber < 0) {
            lineNumber = 0
        }
        val headString = "[ ($className:$lineNumber)#$methodName ]   "
        return arrayOf(TAG, msg, headString)
    }

    private fun printDefault(tag: String, msg: String, write: Boolean) {
        var index = 0
        val length = msg.length
        val countOfSub: Int = length / MAX_LENGTH
        if (countOfSub > 0) {
            for (i in 0 until countOfSub) {
                val sub = msg.substring(index, index + MAX_LENGTH)
                Log.d(tag, sub)
                index += MAX_LENGTH
            }
            Log.d(tag, msg.substring(index, length))
        } else {
            Log.d(tag, msg)
        }

        if (write) write(msg)
    }

    private fun write(content: String) {
        var fileOutputStream: FileOutputStream? = null
        try {
            var date: String = SimpleDateFormat("yyyyMMdd").format(Date())
            var dateTime: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

//            val externalFilesDir = Utils?.context?.externalCacheDir
            val externalFilesDir = CommonUtil?.context?.getExternalFilesDir("logger")
            var file = File(externalFilesDir?.path, "/logger_$date.log")
            fileOutputStream = FileOutputStream(file, true)
            fileOutputStream.write("$dateTime --> $content \n".toByteArray())

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
