package ink.itwo.android.lib

import retrofit2.http.GET

/** Created by wang on 2020/6/25. */
interface API {
    @GET("conf/userInfo.json")
    suspend fun userInfo(): HttpResult<User>

    @GET("conf/banne1r.json")
    suspend fun banner(): HttpResult<Banner>
}