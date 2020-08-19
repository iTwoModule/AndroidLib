package ink.itwo.android.common.control

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import ink.itwo.android.common.ActivityStack

/** Created by wang on 2020/3/26. */
open class CommonApplication : Application(), ActivityLifecycleCallbacks {
    protected var appCount = 0
    override fun onCreate() {
        super.onCreate()
        context = this
        registerActivityLifecycleCallbacks(this)
    }

    fun getCount(): Int {
        return appCount
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        ActivityStack.instance.add(activity as FragmentActivity)
    }

    override fun onActivityStarted(activity: Activity?) {
        if (appCount == 0) {
            hasAppBackground(false)
//            Log.v("Application", "**********切到前台**********");
        }
        appCount++
    }

    override fun onActivityResumed(activity: Activity?) {}
    override fun onActivityPaused(activity: Activity?) {}
    override fun onActivityStopped(activity: Activity?) {
        appCount--
        if (appCount == 0) {
            hasAppBackground(true)
//            Log.v("Application", "**********切到后台**********");
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
        ActivityStack.instance.remove(activity)
    }
    /** app是否后台运行，true :后台运行*/
    fun hasAppBackground(hasBackground: Boolean) {

    }

    companion object {
        lateinit var context: CommonApplication
    }

}