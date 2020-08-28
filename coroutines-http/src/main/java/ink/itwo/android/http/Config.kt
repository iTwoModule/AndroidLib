package ink.itwo.android.http

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

/** Created by wang on 2020/8/20. */
class Config {
    var root_url:String?=null
    var defaultConnectTimeout = 10L
    var defaultWriteTimeout = 10L
    var defaultReadTimeout = 10L
    var cacheFileName = "httpCache"
    var cacheSize = 50L
    var interceptors = mutableListOf<Interceptor>()
    var networkInterceptors = mutableListOf<Interceptor>()
    var converterFactory: Converter.Factory = GsonConverterFactory.create(GsonBuilder().setLenient().create())
}