package ink.itwo.android.common

import android.content.Context
import androidx.fragment.app.FragmentActivity


/** Created by wang on 2020/8/19. */


object Common {
    var context: Context? = null
    var TAG = "iTwo_Log"
    lateinit var imageLoaderDefault: ImageLoader

    @JvmField
    var DEBUG: Boolean = false
    fun init(context: Context, TAG: String = this.TAG, debug: Boolean = false, imageLoaderDefault: ImageLoader) {
        this.context = context
        this.TAG = TAG
        DEBUG = debug
        this.imageLoaderDefault = imageLoaderDefault
    }

    //正则
    const val REGEX_CERT_NO = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}\$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)\$"
    const val REGEX_MOBILE = "[1]\\d{10}"


    var activityCurrent: FragmentActivity? = null
        get() = ActivityStack.instance.get()
}



