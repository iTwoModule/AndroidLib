package ink.itwo.android.common

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import java.util.*

/** Created by wang on 2020/8/19. */
class ActivityStack {
    private val activityStack: Stack<FragmentActivity> by lazy { Stack<FragmentActivity>() }

    companion object {
        val instance: ActivityStack by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ActivityStack() }
    }

    fun add(activity: FragmentActivity) {
        activityStack.add(activity)
    }

    fun get(): FragmentActivity? {
        return activityStackLastElement()
    }

    /** 获取下一个activity  */
    private fun activityStackLastElement(): FragmentActivity? {
        try {
            return activityStack.lastElement()
        } catch (e: Exception) {
        }
        return null
    }

    /** 获取指定activity  */
    operator fun get(cls: Class<*>): FragmentActivity? {
        return activityStack.firstOrNull { it.javaClass == cls }
    }

    /** 移除当前Activity  */
    fun remove() {
        val activity = activityStackLastElement()
        remove(activity)
    }

    /** 移除Activity  */
    fun remove(activity: Activity?) {
        activity?.let { activityStack.remove(it) }
    }

    /** 关闭当前界面  */
    fun finish() {
        val activity = activityStackLastElement()
        finish(activity)
    }

    fun finish(activity: FragmentActivity?) {
        activity?.let {
            activityStack.remove(it)
            it.finish()
        }
    }

    /**
     * 关闭指定界面
     * @param cls 要关闭的cls
     */
    fun finish(cls: Class<*>) {
        activityStack.firstOrNull { it.javaClass == cls }?.let { finish(it) }
    }

    /** 逐层关闭到指定界面  */
    fun finishExceptOne(cls: Class<*>) {
        while (true) {
            val activity = activityStackLastElement() ?: break
            if (activity.javaClass == cls) {
                break
            }
            finish(activity)
        }
    }

    /** 关闭所有页面  */
    fun finishAll() {
        while (true) {
            val activity = activityStackLastElement() ?: break
            finish(activity)
        }
    }
}