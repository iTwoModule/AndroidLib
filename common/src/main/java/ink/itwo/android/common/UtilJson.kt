package ink.itwo.android.common

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/** Created by wang on 2020/8/19. */

//json
val gson: Gson by lazy { GsonBuilder()./*serializeNulls().*/create() }

inline fun <T> String?.toObj(type: Type?=object : TypeToken<T>() {}.type): T? = try {
    gson.fromJson<T>(this, type)
} catch (e: JsonSyntaxException) {
    null
}

inline fun <reified T> JsonElement?.toObj(): T? = try {
    gson.fromJson<T>(this, object : TypeToken<T>() {}.type)
} catch (e: JsonSyntaxException) {
    null
}

//json  on main thread
fun Any?.jsonStr(): String? = gson.toJson(this)