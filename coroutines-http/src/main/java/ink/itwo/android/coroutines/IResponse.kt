package ink.itwo.android.coroutines

import ink.itwo.android.common.RetCode
import ink.itwo.android.common.ServerNetException

/** Created by wang on 2020/8/20. */
interface IResponse<T> {
    fun responseSuccess(): Boolean
    fun responseCode(): Int?
    fun responseMessage(): String?
    fun responseData():T?
}
/*

fun <T> IResponse<T>.take(): T? {
    if (!responseSuccess()) {
        throw ServerNetException(responseCode() ?: RetCode.UNKNOWN, responseMessage() ?: "")
    }
    return this.responseData()
}

*/
