package ink.itwo.android.common

import android.content.Context
import android.content.SharedPreferences
import ink.itwo.android.common.Common.context
import java.lang.reflect.Type

/** Created by wang on 1/24/21. */

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

fun Common.cachePut(key: String, value: Any) = sp?.edit {
    when (value) {
        is Long -> putLong(key, value)
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Boolean -> putBoolean(key, value)
        is Float -> putFloat(key, value)
        else -> putString(key, value.jsonStr())
    }
}

fun <T> Common.cacheGet(key: String, default: T): T? = sp.run {
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

fun <T> Common.cacheGet(key: String, type: Type): T? = sp.run {
    val string = this?.getString(key, null)
    string?.toObj<T>(type)
}

fun Common.cacheRemove(key: String) = sp?.edit { remove(key) }
fun Common.contains(key: String) = sp?.run { this?.contains(key) }

