package ink.itwo.android.lib

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import ink.itwo.android.common.ActivityStack
import ink.itwo.android.common.CommonUtil
import ink.itwo.android.http.Config
import ink.itwo.android.http.HttpManager

/** Created by wang on 2020/8/19. */
class APP : Application(), Application.ActivityLifecycleCallbacks {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        CommonUtil.init(this,debug = true)
        HttpManager.instance.init(Config().apply {
            root_url="http://files.itwo.ink/"
//            root_url="http://127.0.0.1:8080/apk/"
        })
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
}