package ink.itwo.android.lib

import ink.itwo.android.common.RetCode
import ink.itwo.android.common.ServerNetException
import ink.itwo.android.http.IResponse

/** Created by wang on 2020/8/20. */
class HttpResult<T> : IResponse<T> {
    var code: Int? = null
    var data: T? = null
    var message: String? = null

    override fun responseSuccess() = code == 0
    override fun responseCode() = code
    override fun responseMessage() = message
    override fun responseData()=data
}

fun <T> IResponse<T>.take(): T? {
    if (!responseSuccess()) {
        throw ServerNetException(responseCode() ?: RetCode.UNKNOWN, responseMessage() ?: "")
    }
    return this.responseData()
}