package ink.itwo.android.coroutines

/** Created by wang on 2020/8/20. */
interface IResponse {
    var responseSuccess: Boolean
    var responseCode: Int
    var responseMessage: String

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
