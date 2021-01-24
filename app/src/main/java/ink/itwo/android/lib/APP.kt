package ink.itwo.android.lib

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import ink.itwo.android.common.ActivityStack
import ink.itwo.android.common.CommonUtil
import ink.itwo.android.common.ImageLoader
import ink.itwo.android.coroutines.Config
import ink.itwo.android.coroutines.NetManager

/** Created by wang on 2020/8/19. */
class APP : Application(), Application.ActivityLifecycleCallbacks {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        CommonUtil.init(this,debug = true,imageLoaderDefault = imageLoader)
        NetManager.init(this,Config().apply {
            root_url="http://files.itwo.ink/"
//            root_url="http://127.0.0.1:8080/apk/"
        },CommonUtil.executorCoroutineDispatcher)
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

    var imageLoader= object : ImageLoader {
        override fun displayImage(activity: Context?, path: Any?, imageView: ImageView?, width: Int, height: Int) {
        }

        override fun displayImage(context: Context?, url: Any?, imageView: ImageView?) {
        }

        override fun clearMemoryCache() {
        }

    }
}

val api:API by lazy { NetManager.http.create(API::class.java) }