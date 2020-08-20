package ink.itwo.android.coroutines

import ink.itwo.android.common.CommonException

/** Created by wang on 2020/8/20. */
open class NetException(code: Int,message: String) :CommonException(message)

open class ClientNetException(code: Int, message: String) : NetException(code, message) {

}
open class ServerNetException(code: Int, message: String) : NetException(code, message) {

}

