package ink.itwo.android.http.file

import ink.itwo.android.common.RetCode
import ink.itwo.android.common.ktx.log
import ink.itwo.android.http.NetManager.context
import ink.itwo.android.http.NetManager.executorCoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
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

    /**
     *  单文件下载
     * @param info DownLoadInfo
     * @param interceptors MutableList<Interceptor>?
     * @return DownLoadResult
     */
    suspend fun down(info: DownLoadInfo, interceptors: MutableList<Interceptor>? = null): DownLoadResult {
        return suspendCoroutine<DownLoadResult> {
            val result = downInternal(info, interceptors)
            it.resume(result)
        }
    }

    /**
     *  并发下载多个文件
     * @param infoList MutableList<DownLoadInfo>  下载文件的信息
     * @param interceptors MutableList<Interceptor>? 拦截器，可设置 token 等拦截器
     * @return MutableList<DownLoadResult>
     */
    suspend fun downMulti(infoList: MutableList<DownLoadInfo>, interceptors: MutableList<Interceptor>? = null): MutableList<DownLoadResult> {
        var list = withContext(Dispatchers.Default) {
            infoList.map { info -> async(executorCoroutineDispatcher) { down(info) } }
        }.toMutableList().map { it.await() }.toMutableList()
        return suspendCoroutine<MutableList<DownLoadResult>> {
            it.resume(list)
        }
    }

    /**
     *
     * @param downLoadInfo DownLoadInfo 下载请求的信息
     * @param interceptors MutableList<Interceptor>? 请求拦截器，可设置 token 等拦截器
     * @return DownLoadResult 下载结果
     */
    internal fun downInternal(downLoadInfo: DownLoadInfo, interceptors: MutableList<Interceptor>? = null): DownLoadResult {

        var result = DownLoadResult(url = downLoadInfo.url, result = true, code = RetCode.SUCCESS, key = downLoadInfo.key)

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
        var path = downLoadInfo.path?.let { it } ?: context?.getExternalFilesDir("down")?.absolutePath + File.separator + (downLoadInfo.url.split("/").lastOrNull())
        var file = File(path)
        if (file.parentFile?.exists() != true) {
            val mkdirs = file.parentFile?.mkdirs()
            "file.parentFile mkdirs $mkdirs".log()
        }
        if (file.exists() && downLoadInfo.delOld == true) {
            val delete = file.delete()
            "old delete $delete".log()
        }
        val outputStream = file.outputStream()
        val byteStream = response.body?.byteStream()
        byteStream?.copyTo(outputStream)
        outputStream.close()
        byteStream?.close()
        result.path = path
        return result
    }

}

/**
 *
 * @property key String? 每个下载请求的唯一key
 * @property url String 下载地址
 * @property path String? 保存路径
 * @property progressListener Function3<Long, Long, Boolean, Unit>? 下载进度监听
 * @property delOld Boolean? 是否删除原有文件
 * @constructor
 */
class DownLoadInfo constructor(var key: String? = null, val url: String, val path: String? = null, val progressListener: ((Long, Long, Boolean) -> Unit)? = null, val delOld: Boolean? = true)

/**
 *
 * @property key String? 每个下载请求的唯一 key ，同 [DownLoadInfo] 中的key
 * @property url String  下载地址，同 [DownLoadInfo] 中的 url
 * @property path String? 下载文件保存的地址，[DownLoadInfo] 中的 path 为空时，该 path = context.getExternalFilesDir
 * @property result Boolean  下载结果
 * @property code Int  返回值
 * @property message String?  错误信息
 * @constructor
 */
class DownLoadResult constructor(var key: String? = null, var url: String, var path: String? = null, var result: Boolean, var code: Int, var message: String? = null)
