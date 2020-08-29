package ink.itwo.android.http.file

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okio.*

/** Created by wang on 2020/8/29. */
class DownloadProgressInterceptor constructor(private val listener: ((bytesRead: Long, contentLength: Long, done: Boolean) -> Unit)? = null) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val body = originalResponse.body
        return body?.let {
            originalResponse.newBuilder().body(DownloadProgressResponseBody(it, listener)).build()
        } ?: originalResponse
    }

}

class DownloadProgressResponseBody constructor(private val body: ResponseBody, private val listener: ((bytesRead: Long, contentLength: Long, done: Boolean) -> Unit)?) : ResponseBody() {

    private val bufferedSource: BufferedSource by lazy { source(body.source()).buffer() }
    override fun contentLength(): Long = body.contentLength()

    override fun contentType(): MediaType? = body.contentType()

    override fun source(): BufferedSource {
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead: Long = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                listener?.invoke(totalBytesRead, contentLength(), bytesRead == -1L)
                return bytesRead
            }
        }
    }

}

