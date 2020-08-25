package ink.itwo.android.lib

import retrofit2.http.GET

/** Created by wang on 2020/6/25. */
interface API {
    @GET("conf/userInfo.json")
    suspend fun userInfo(): HttpResult<User>

    @GET("conf/banne1r.json")
    suspend fun banner(): HttpResult<Banner>

    @GET("http://192.168.88.101:8080/apk/delay")
    suspend fun delay(): HttpResult<MutableMap<String, String>>
}