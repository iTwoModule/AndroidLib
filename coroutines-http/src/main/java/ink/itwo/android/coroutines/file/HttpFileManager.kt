package ink.itwo.android.coroutines.file

import android.os.Looper
import ink.itwo.android.coroutines.NetManager.context
import ink.itwo.android.coroutines.NetManager.executorCoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/** Created by wang on 2020/8/29. */
class HttpFileManager {
    private val okHttpClientBuilder: OkHttpClient.Builder by lazy {
        OkHttpClient.Builder().apply {
            connectTimeout(15, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
        }
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { HttpFileManager() }
    }

    /**
     *  单文件下载
     * @param info DownLoadInfo
     * @param interceptors MutableList<Interceptor>?
     * @return DownLoadResult
     */
    suspend fun down(info: DownLoadInfo, interceptors: MutableList<Interceptor>? = null): DownLoadResult {
        val result = withContext(Dispatchers.IO) { downInternal(info, interceptors) }
        return suspendCoroutine<DownLoadResult> {
            it.resume(result)
        }
    }

    /*
     lifecycleScope.launch {
            val infoList = urls.mapIndexed { index, s ->
                DownLoadInfo(url = s, progressListener = { c, a, b ->
                    "$index  bytesRead $c  contentLength $a  done $b   threadId ${Thread.currentThread().id}".log()
                })
            }.toMutableList()
            val resultList = NetManager.down.downMulti(infoList)
     */
    /**
     *
     *  并发下载多个文件
     * @param infoList MutableList<DownLoadInfo>  下载文件的信息
     * @param interceptors MutableList<Interceptor>? 拦截器，可设置 token 等拦截器
     * @return MutableList<DownLoadResult>
     */
    suspend fun downMulti(infoList: MutableList<DownLoadInfo>, interceptors: MutableList<Interceptor>? = null): MutableList<DownLoadResult> {
        var list = withContext(Dispatchers.Default) {
            infoList.map { info -> async(executorCoroutineDispatcher) { down(info, interceptors) } }
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
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw RuntimeException("run on ui thread!")
        }
        var result = DownLoadResult(url = downLoadInfo.url, result = true, code = 200, key = downLoadInfo.key)
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
//            "file.parentFile mkdirs $mkdirs".log()
        }
        if (file.exists() && downLoadInfo.delOld == true) {
            val delete = file.delete()
        }
        val outputStream = file.outputStream()
        val byteStream = response.body?.byteStream()
        byteStream?.copyTo(outputStream)
        outputStream.close()
        byteStream?.close()
        result.path = path
        return result
    }

    /** 文件上传*/
    suspend fun up(info: UploadInfo,funFileMimeType:((File)->String?)?=null): UploadResult {
        var result = withContext(Dispatchers.IO) { upInternal(info,funFileMimeType) }
        return suspendCoroutine {
            it.resume(result)
        }
    }


    /*
        lifecycleScope.launch {
            var infoList = paths.mapIndexed { index,it->
                UploadInfo(url, UUID.randomUUID().toString()) { a, b, c ->
                    "$index  bytesRead $c  contentLength $a  done $b   threadId ${Thread.currentThread().id}".log()
                }.addFile(File(it)).addParams("name", "name1").addParams("age", 1)
            }.toMutableList()
            val upMulti = NetManager.file.upMulti(infoList)
            upMulti.jsonStr().log()
        }
        }
    * */

    /** 并发上传*/
    suspend fun upMulti(infoList: MutableList<UploadInfo>,funFileMimeType:((File)->String?)?=null): MutableList<UploadResult> {
        var list = withContext(Dispatchers.Default) {
            infoList.map { info -> async(executorCoroutineDispatcher) { upInternal(info,funFileMimeType) } }.toMutableList().map { it.await() }.toMutableList()
        }
        return suspendCoroutine { it.resume(list) }
    }

    /** 一个请求上传多个文件*/
    internal fun upInternal(info: UploadInfo,funFileMimeType:((File)->String?)?=null): UploadResult {

        var result = UploadResult(key = info.key ?: info.url, path = info.files.map { it.path }.toMutableList(), result = true, code = 200)
        if (info.files.isNullOrEmpty()) {
            result.message = "files is empty"
            return result
        }

        var client = okHttpClientBuilder.build()
        val request = Request.Builder()
        request.url(info.url)
        val multipartBodyBuilder = MultipartBody.Builder()
        info.files.forEach { f ->
            var requestBody: RequestBody
            val body = f.asRequestBody((funFileMimeType?.invoke(f) ?: "application/octet-stream").toMediaType())
            requestBody = if (info.progressListener != null) {
                var bodyProgress = UploadProgressResponseBody(body, info.progressListener)
                bodyProgress
            } else {
                body
            }
            multipartBodyBuilder.addFormDataPart(info.name ?: "file", filename = f.name, body = requestBody)
        }
        info.params?.forEach { map ->
            multipartBodyBuilder.addFormDataPart(map.key, map.value.toString())
        }

        info.headers?.let { request.headers(it.build()) }
        request.post(multipartBodyBuilder.build())
        val response = client.newCall(request.build()).execute()
        result.code = response.code
        result.message = response.message
        if (!response.isSuccessful) {
            result.result = false
            return result
        }
        result.response = response.body?.string() ?: ""
        return result
    }
}

/**
 *
 * @property key String? 每个下载请求的唯一key
 * @property url String 下载地址
 * @property path String? 保存路径
 * @property progressListener Function3<Long, Long, Boolean, Unit>? 下载进度监听 <已下载进度，总大学，是否完成>
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

/**
 *  上传信息组装
 * @property url String  上传的目标地址
 * @property key String?  每个请求的key，在上传结果中会返回该 key
 * @property name String? 上传文件的参数名
 * @property progressListener Function3<Long, Long, Boolean, Unit>? 上传进度 <已上传，总大小，是否完成>
 * @property params MutableMap<String, Any>?  请求的参数
 * @property headers Builder?  每个请求的 headers ，可设置 token
 * @property files MutableList<File>  上传文件的 list
 * @constructor
 */
class UploadInfo constructor(val url: String, val key: String? = null, val name: String? = null, val progressListener: ((Long, Long, Boolean) -> Unit)? = null) {
    val params: MutableMap<String, Any>? by lazy { mutableMapOf<String, Any>() }
    val headers: Headers.Builder? by lazy { Headers.Builder() }
    val files = mutableListOf<File>()

    fun addParams(key: String, value: Any) = apply { params?.set(key, value) }
    fun addHeader(key: String, value: String) = apply { headers?.add(key, value) }
    fun addFile(file: File) = apply { files.add(file) }
}

/**
 *  上传结果
 * @property key String?  每个请求的唯一 key ,同 [UploadInfo] 的key
 * @property path MutableList<String> 上传文件的本地路径
 * @property result Boolean 请求结果
 * @property code Int 返回的状态码
 * @property message String? 返回的错误信息
 * @property response String?  接口返回的结果
 * @constructor
 */
class UploadResult constructor(var key: String? = null, var path: MutableList<String>, var result: Boolean, var code: Int, var message: String? = null, var response: String? = null)