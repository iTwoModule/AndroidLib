package ink.itwo.android.common

import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
/** Created by wang on 1/24/21. */



open class CommonException(message: String) : Exception(message)
open class NetException(code: Int, message: String) : CommonException(message)
open class ClientNetException(code: Int, message: String) : NetException(code, message)
open class ServerNetException(code: Int, message: String) : NetException(code, message)

fun Throwable.handlerException(): Throwable {
    return when (this) {
        is ClientNetException -> {
            this
        }
        is JsonParseException, is JSONException, is ParseException, is JsonSyntaxException -> {
            ClientNetException(RetCode.PARSE_ERROR, "数据错误")
        }
        is ConnectException -> {
            ClientNetException(RetCode.NETWORK_ERROR, "网络错误")
        }
        is UnknownHostException -> {
            ClientNetException(RetCode.NETWORK_ERROR, "网络错误")
        }
        is SocketTimeoutException -> {
            ClientNetException(RetCode.NETWORK_ERROR, "网络错误")
        }
        is HttpException -> {
            val code = this.code()
            if (code == 500) {
                ClientNetException(this.code(), "服务器异常")
            } else {
                ClientNetException(this.code(), message())
            }
        }
        else -> this
    }
}

object RetCode {
    //HTTP 状态
    /** 成功  */
    const val SUCCESS = 200

    /** 错误请求，或者参数错误  */
    const val BAD_REQUEST = 400

    /** 接口不能到达  */
    const val NOT_FOUND = 404

    /** 服务器错误  */
    const val SERVER_ERROR = 500

    /** 不能及时得到响应时返回此错误代码  */
    const val GATEWAY_TIMEOUT = 504

    /** 服务器不支持请求中所使用的HTTP协议版本  */
    const val HTTP_Not_Supported = 505
    //自定义状态码
    /** 未知错误  */
    const val UNKNOWN = 32100

    /** json解析错误  */
    const val PARSE_ERROR = 32101

    /** 网络错误  */
    const val NETWORK_ERROR = 32102
}

