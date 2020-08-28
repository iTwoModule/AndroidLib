package ink.itwo.android.http

import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

/** Created by wang on 2020/8/20. */
class IntAdapter : TypeAdapter<Int?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Int?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value)
        }
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Int? {
        return when (val peek = `in`.peek()) {
            JsonToken.NULL -> null
            JsonToken.STRING -> {
                var integer: Int? = null
                try {
                    integer = `in`.nextString().toInt()
                    return integer
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                null
            }
            else -> throw JsonParseException("Expected Integer or NUMBER but was $peek")
        }
    }
}

class BooleanAdapter : TypeAdapter<Boolean?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Boolean?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value)
        }
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Boolean? {
        return when (val peek = `in`.peek()) {
            JsonToken.NUMBER -> `in`.nextInt() == 1 //如果为true则返回为int的1，false返回0.
            JsonToken.NULL -> false
            JsonToken.STRING -> "1".equals(`in`.nextString(), ignoreCase = true)
            else -> throw JsonParseException("Expected BOOLEAN or NUMBER but was $peek")
        }
    }
}
/*
data class MessageSystem(
        var addTime: String? = "",
        var content: String? = "",
        var id: Int? = 0,
        //0:未读 1:已读
        @JsonAdapter(BooleanAdapter::class) var isRead: Boolean? = false,
        var title: String? = "",
        var type: String? = ""
        ) : Serializable
 */