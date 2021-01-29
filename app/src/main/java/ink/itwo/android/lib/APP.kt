package ink.itwo.android.lib

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import ink.itwo.android.common.*
import ink.itwo.android.coroutines.Config
import ink.itwo.android.coroutines.NetManager
import ink.itwo.android.coroutines.ktx.CoroutinesKtx
import kotlinx.coroutines.asCoroutineDispatcher
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.ScheduledThreadPoolExecutor

/** Created by wang on 2020/8/19. */
class APP : Application(), Application.ActivityLifecycleCallbacks {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        Common.init(this, debug = true, imageLoaderDefault = imageLoader)
        var config = object : Config() {
            override val DEBUG = BuildConfig.DEBUG
            override val context = this@APP
            override val root_url = "http://files.itwo.ink/"
        }
        NetManager.init(this, config, ScheduledThreadPoolExecutor(Common.maximumPoolSize).asCoroutineDispatcher())

        CoroutinesKtx.init(handlerException = handlerException, toastInvoke = toastInvoke)
    }

    var handlerException = object : (Exception) -> Exception {
        override fun invoke(p1: Exception): Exception {
            return when (p1) {
                is ClientException -> {
                    p1;
                }
                is JsonParseException, is JSONException, is ParseException, is JsonSyntaxException -> {
                    ClientException(RetCode.PARSE_ERROR, "数据错误")
                }
                is ConnectException -> {
                    ClientException(RetCode.NETWORK_ERROR, "网络错误")
                }
                is UnknownHostException -> {
                    ClientException(RetCode.NETWORK_ERROR, "网络错误")
                }
                is SocketTimeoutException -> {
                    ClientException(RetCode.NETWORK_ERROR, "网络错误")
                }
                is HttpException -> {
                    when (p1.code()) {
                        500 -> ServerException(p1.code(), "服务器异常")
                        404 -> ServerException(p1.code(), "网络错误")
                        else -> p1
                    }
                }
                else -> CommonException(p1.message ?: "")
            }
        }
    }
    var toastInvoke = object : ((String) -> Unit) {
        override fun invoke(p1: String) {
            p1.toast()
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        ActivityStack.instance.remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ActivityStack.instance.add(activity as FragmentActivity)
    }

    override fun onActivityResumed(activity: Activity) {
    }

    var imageLoader = object : ImageLoader {
        override fun displayImage(activity: Context?, path: Any?, imageView: ImageView?, width: Int, height: Int) {
        }

        override fun displayImage(context: Context?, url: Any?, imageView: ImageView?) {
        }

        override fun clearMemoryCache() {
        }

    }
}

val api: API by lazy { NetManager.http.create(API::class.java) }