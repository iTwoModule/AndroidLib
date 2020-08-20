package ink.itwo.android.lib

import ink.itwo.android.coroutines.IResponse

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
