package ink.itwo.android.http

import ink.itwo.android.common.CommonUtil
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit

/** Created by wang on 2020/8/20. */
class HttpManager private constructor() {
    private val retrofitBuilder: Retrofit.Builder by lazy { Retrofit.Builder() }
    private val okHttpClientBuilder: OkHttpClient.Builder by lazy { OkHttpClient.Builder() }
    private lateinit var retrofit: Retrofit

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { HttpManager() }
    }

    internal fun init(config: Config) {
        config.root_url?.let { retrofitBuilder.baseUrl(it) } ?: throw ExceptionInInitializerError("请设置root_url")
        retrofitBuilder.addConverterFactory(config.converterFactory)

        okHttpClientBuilder.connectTimeout(config.defaultConnectTimeout, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(config.defaultWriteTimeout, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(config.defaultReadTimeout, TimeUnit.SECONDS)

        okHttpClientBuilder.cache(Cache(File(CommonUtil.context?.cacheDir, config.cacheFileName), config.cacheSize * 1024 * 1024))

        config.interceptors.forEach { okHttpClientBuilder.addInterceptor(it) }
        config.networkInterceptors.forEach { okHttpClientBuilder.addNetworkInterceptor(it) }
        if (CommonUtil.DEBUG) okHttpClientBuilder.addInterceptor(LoggingInterceptor())
        retrofitBuilder.client(okHttpClientBuilder.build())
        retrofit = retrofitBuilder.build()
    }
    fun <T> create(cls: Class<T>): T {
        return retrofit.create(cls)
    }
}