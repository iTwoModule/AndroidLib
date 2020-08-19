package ink.itwo.android.common

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import ink.itwo.android.common.CommonUtil.Companion.gson
import java.io.Serializable
import java.lang.reflect.Type


/** Created by wang on 2020/8/19. */


class CommonUtil {
    companion object {
        var context: Context? = null
        var TAG = "iTwo_Log"
        var DEBUG: Boolean = false
        fun init(context: Context, TAG: String = Companion.TAG, debug: Boolean = false) {
            Companion.context = context
            Companion.TAG = TAG
            DEBUG = debug
        }


        //正则
        const val REGEX_CERT_NO = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}\$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)\$"
        const val REGEX_MOBILE = "[1]\\d{10}"


        var currentActivity:FragmentActivity?=null
            get() =  ActivityStack.instance.get()


        //  context
         fun getPackageInfo(): PackageInfo? = context?.packageName?.let { currentActivity?.packageManager?.getPackageInfo(it, PackageManager.GET_CONFIGURATIONS) }

        val versionName by lazy { getPackageInfo()?.versionName }
        val versionCode by lazy { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) getPackageInfo()?.longVersionCode else getPackageInfo()?.versionCode?.toLong() }
        fun toPlayStore(applicationId: String) {
            try {
                val uri = Uri.parse("market://details?id=${applicationId}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                currentActivity?.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //json
        val gson: Gson by lazy { GsonBuilder()./*serializeNulls().*/create() }

        inline fun <reified T> String?.toObj(): T? = try {
            gson.fromJson<T>(this, object : TypeToken<T>() {}.type)
        } catch (e: JsonSyntaxException) {
            null
        }

        inline fun <reified T> JsonElement?.toObj(): T? = try {
            gson.fromJson<T>(this, object : TypeToken<T>() {}.type)
        } catch (e: JsonSyntaxException) {
            null
        }


        // cache
        val sp: SharedPreferences? by lazy { context?.getSharedPreferences(context?.packageName ?: "", Context.MODE_PRIVATE) }

        fun SharedPreferences.edit(commit: Boolean = false, action: SharedPreferences.Editor.() -> Unit): Boolean? {
            val editor = edit()
            action(editor)
            return if (commit) {
                editor.commit()
            } else {
                editor.apply()
                true
            }
        }

        fun cachePut(key: String, value: Any) = sp?.edit {
            when (value) {
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                else -> putString(key, value.toJson())
            }
        }

        fun <T> cacheGet(key: String, default: T): T? = sp.run {
            var res = when (default) {
                is Int -> this?.getInt(key, default)
                is Long -> this?.getLong(key, default)
                is Boolean -> this?.getBoolean(key, default)
                is Float -> this?.getFloat(key, default)
                is String -> this?.getString(key, default)
                else -> null
            }
            res as? T
        }

        inline fun <reified T> cacheGet(key: String, type: Type): T? = sp.run {
            val string = this?.getString(key, null)
            string?.toObj<T>()
        }

        fun cacheRemove(key: String) = sp?.edit { remove(key) }
        fun contains(key: String) = sp?.run { this?.contains(key) }


        //device
        val dm: DisplayMetrics by lazy { DisplayMetrics().apply { currentActivity?.windowManager?.defaultDisplay?.getRealMetrics(this) } }
        /** 屏幕宽度*/
        val deviceWith: Int by lazy { dm.widthPixels }
        /** 屏幕高度*/
        val deviceHeight: Int by lazy {   dm.heightPixels }
        /** dp 换 px*/
        fun toPx(dpValue: Float): Int {
            val scale = context?.resources?.displayMetrics?.density ?: 0F
            return (dpValue * scale + 0.5f).toInt()
        }

        /** px 换 dp*/
        fun toDp(pxValue: Float): Int {
            val scale = context?.resources?.displayMetrics?.density ?: 0F
            return (pxValue / scale + 0.5f).toInt()
        }

        /** 从Dimension中转换成PX  */
        fun dimenToPx(@DimenRes id: Int): Int {
            return context?.resources?.getDimension(id)?.toInt() ?: 0
        }

        /** 从Dimension中转换成DP  */
        fun dimenToDp(@DimenRes id: Int): Int {
            return toDp(context?.resources?.getDimension(id) ?: 0F)
        }
    }


}

/////////////////       全局      /////////////
//json
fun Any?.toJson(): String? = gson.toJson(this)

//toast
private var sToast: Toast? = Toast.makeText(CommonUtil.context, "", Toast.LENGTH_LONG)
//private var sToast:Toast?=null
private val defaultYOffset = sToast?.yOffset
fun String?.toast(gravity: Int = Gravity.BOTTOM, xOffset: Int = 0, yOffset: Int = defaultYOffset?:0, view: View? = null) {
    if (sToast == null) sToast = Toast.makeText(CommonUtil.context, null, Toast.LENGTH_LONG)
    else  {
        sToast?.cancel()
        sToast = Toast.makeText(CommonUtil.context, "", Toast.LENGTH_LONG)
//        sToast?.setText(this)
    }
    sToast?.setText(this)
    sToast?.setGravity(gravity, xOffset, yOffset)
    view?.let { sToast?.view = view }
    sToast?.show()
}



class CommonException : Exception()


interface ImageLoader : Serializable {
    fun displayImage(activity: Context?, path: Any?, imageView: ImageView?, width: Int, height: Int)

    fun displayImage(context: Context?, url: Any?, imageView: ImageView?)

    fun clearMemoryCache()
}