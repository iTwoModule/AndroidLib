package ink.itwo.android.lib

import retrofit2.http.GET

/** Created by wang on 2020/6/25. */
interface API {
    @GET("json/success_user.json")
    suspend fun userInfoSuccess(): HttpResult<User>

    @GET("json/error_user.json")
    suspend fun userInfoError(): HttpResult<User>
    @GET("json/error_use11r.json")
    suspend fun netWorkError(): HttpResult<User>

}