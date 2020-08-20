package ink.itwo.android.coroutines;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import ink.itwo.android.common.CommonUtil;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;


/**
 Created by wang on 17/9/26. */

public class LoggingInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private StringBuffer stringBuffer = new StringBuffer();
    private String params;
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final JsonParser PARSER = new JsonParser();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        stringBuffer.delete(0, stringBuffer.length());
        {
            RequestBody requestBody = request.body();
            boolean hasRequestBody = requestBody != null;

            Connection connection = chain.connection();
            Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
            stringBuffer
                    .append("\n")
                    .append("\n")
                    .append("------------------------------------------------------")
                    .append("\n")
                    .append(request.method())
                    .append("\n")
                    .append(protocol);

            if (hasRequestBody) {
                if (requestBody.contentType() != null) {
                    stringBuffer.append("\n").append("Content-Type:").append(requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    stringBuffer
                            .append("\n")
                            .append("Content")
                            .append(requestBody.contentLength());
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    stringBuffer
                            .append("\n")
                            .append(name + ": " + headers.value(i));
                }
            }

            if (!hasRequestBody) {
                stringBuffer
                        .append("\n")
                        .append("--> END" + request.method());
            } else if (bodyEncoded(request.headers())) {
                stringBuffer
                        .append("\n")
                        .append("--> END" + request.method());
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (isPlaintext(buffer)) {
                    final String bufferString = buffer.readString(charset);
                    params = bufferString;
                    stringBuffer
                            .append("\n")
                            .append("request : " + bufferString);
                    if (contentType != null && "json".equals(contentType.subtype())) {
                        stringBuffer
                                .append("\n")
                                .append(GSON.toJson(PARSER.parse(bufferString)));
                    }
                    stringBuffer.append("\n")
                            .append("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
                } else {
                    stringBuffer
                            .append("\n")
                            .append("--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)");
                }
            }
        }

        {
            long startNs = System.nanoTime();
            Response response = null;
            try {
                response = chain.proceed(request);
            } catch (Exception e) {
                log(e.toString());
            }
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            stringBuffer
                    .append("\n")
                    .append(response.code()).append(" ")
                    .append(response.message())
                    .append("\n")
                    .append(response.request().url()).append("?").append(params);
            stringBuffer
                    .append("\n")
                    .append(tookMs)
                    .append(" ms");
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                stringBuffer
                        .append("\n")
                        .append(headers.name(i))
                        .append(" : ")
                        .append(headers.value(i));
            }

            if (!HttpHeaders.hasBody(response)) {
                stringBuffer
                        .append("\n")
                        .append("<-- END HTTP");
            } else if (bodyEncoded(response.headers())) {
                stringBuffer
                        .append("\n")
                        .append("<-- END HTTP (encoded body omitted)");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8);
                    } catch (UnsupportedCharsetException e) {
                        stringBuffer
                                .append("\n")
                                .append("Couldn't decode the response body; charset is likely malformed.")
                                .append("\n")
                                .append("<-- END HTTP");
                        return response;
                    }
                }

                if (!isPlaintext(buffer)) {
                    stringBuffer
                            .append("\n")
                            .append("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                    return response;
                }

                if (contentLength != 0) {
                    final String bufferString = buffer.clone().readString(charset);
                    stringBuffer
                            .append("\n")
                            .append("body : ")
                            .append("\n")
                            .append("\n")
                            .append(bufferString)
                            .append("\n")
                            .append("\n")
                            .append("json : ")
                            .append("\n")
                            .append(formatJson(bufferString));
                    if (contentType != null && "json".equals(contentType.subtype())) {
                        stringBuffer
                                .append("\n")
                                .append("json")
                                .append(GSON.toJson(PARSER.parse(bufferString)));
                    }
                }
                stringBuffer
                        .append("\n")
                        .append("------------------------------------------------------")
                        .append("\n");
            }
            log(stringBuffer.toString());
            return response;
        }
    }


    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    /**
     格式化json字符串
     @param jsonStr 需要格式化的json串
     @return 格式化后的json串
     */
    private String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            //遇到{ [换行，且下一行缩进
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                //遇到} ]换行，当前行缩进
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                //遇到,换行
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    /**
     添加space
     @param sb
     @param indent
     */
    private void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    public static void log(String message) {
        if (!CommonUtil.DEBUG) return;
        if (message != null && message.length() > 4000) {
            split("iTwo_http", message);
        } else {
            Log.d("iTwo_http", message);
        }
    }

    public static void split(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 4000;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.d(tag, msg);
        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                Log.d(tag, logContent);
            }
            Log.d(tag, msg);// 打印剩余日志
        }
    }

}
