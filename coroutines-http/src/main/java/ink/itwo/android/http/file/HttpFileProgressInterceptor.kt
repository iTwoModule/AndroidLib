package ink.itwo.android.http.file

import okhttp3.*
import okio.*
import java.io.IOException

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

class UploadProgressResponseBody constructor(private val body: RequestBody, private val listener: ((bytesRead: Long, contentLength: Long, done: Boolean) -> Unit)?) : RequestBody() {
    override fun contentType() = body.contentType()
    override fun contentLength() = body.contentLength()
    override fun writeTo(sink: BufferedSink) {
        var countingSink = CountingSink(sink) {
            listener?.invoke(it, contentLength(), it == contentLength())
        }
        var bufferedSink = countingSink.buffer()
        body.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    private class CountingSink(delegate: Sink, private val listener: ((bytesRead: Long) -> Unit)?) : ForwardingSink(delegate) {
        private var bytesWritten: Long = 0
        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            bytesWritten += byteCount
            listener?.invoke(bytesWritten)
        }
    }
}



