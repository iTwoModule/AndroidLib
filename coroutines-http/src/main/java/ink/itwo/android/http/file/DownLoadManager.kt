package ink.itwo.android.http.file

import ink.itwo.android.common.RetCode
import ink.itwo.android.common.ktx.log
import ink.itwo.android.http.NetManager.context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/** Created by wang on 2020/8/29. */
class DownLoadManager {
    private val okHttpClientBuilder: OkHttpClient.Builder by lazy {
        OkHttpClient.Builder().apply {
            connectTimeout(15, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
        }
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { DownLoadManager() }
    }

    suspend fun down(info: DownLoadInfo, interceptors: MutableList<Interceptor>? = null): DownLoadResult {
        return suspendCoroutine<DownLoadResult> {
            val result = downInternal(info, interceptors)
            it.resume(result)
        }
    }

    suspend fun downMulti(infoList: MutableList<DownLoadInfo>, interceptors: MutableList<Interceptor>? = null): MutableList<DownLoadResult> {
        return suspendCoroutine<MutableList<DownLoadResult>> {

            var list = infoList.map { info -> downInternal(info) }.toMutableList()
            it.resume(list)
        }
    }

    internal fun downInternal(downLoadInfo: DownLoadInfo, interceptors: MutableList<Interceptor>? = null): DownLoadResult {

        var result = DownLoadResult(url = downLoadInfo.url, result = true, code = RetCode.SUCCESS)

        interceptors?.forEach { okHttpClientBuilder.addInterceptor(it) }
        downLoadInfo.progressListener?.let { okHttpClientBuilder.addInterceptor(DownloadProgressInterceptor(it)) }
        var client = okHttpClientBuilder.build()
        val call = client.newCall(Request.Builder().url(downLoadInfo.url).build())
        val response = call.execute()
        if (!response.isSuccessful) {
            result.message = response.message
            result.code = response.code
            result.result = false
            return result
        }
        var path = downLoadInfo.path?.let { it } ?: context?.getExternalFilesDir("down")?.absolutePath + (File.separator + downLoadInfo.url.split("/").lastOrNull())
        var file = File(path)
        if (file.parentFile?.exists() != true) {
            val mkdirs = file.parentFile?.mkdirs()
            "file.parentFile mkdirs $mkdirs".log()
        }
        if (file.exists() && downLoadInfo.delOld == true) {
            val delete = file.delete()
            "old delete $delete".log()
        }
        response.body?.byteStream()?.copyTo(file.outputStream())
        result.path = path
        return result
    }

}

class DownLoadInfo constructor(val url: String, val path: String? = null, val progressListener: ((Long, Long, Boolean) -> Unit)? = null, val delOld: Boolean? = true)
class DownLoadResult constructor(var url: String, var path: String? = null, var result: Boolean, var code: Int, var message: String? = null)
