package ink.itwo.android.common.control

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import ink.itwo.android.common.ActivityStack

/** Created by wang on 2020/3/26. */
open class CommonApplication : Application(), Application.ActivityLifecycleCallbacks {
    protected var appCount = 0
    override fun onCreate() {
        super.onCreate()
        context = this
        registerActivityLifecycleCallbacks(this)
    }


    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
        if (appCount == 0) {
            hasAppBackground(false)
            //            Log.v("Application", "**********切到前台**********");
        }
        appCount++
    }

    override fun onActivityDestroyed(activity: Activity) {
        ActivityStack.instance.remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
        appCount--
        if (appCount == 0) {
            hasAppBackground(true)
            //            Log.v("Application", "**********切到后台**********");
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ActivityStack.instance.add(activity as FragmentActivity)
    }

    override fun onActivityResumed(activity: Activity) {
    }

    fun getCount(): Int {
        return appCount
    }

    open fun hasAppBackground(hasBackground: Boolean) {

    }

    companion object {
        lateinit var context: CommonApplication
    }

}