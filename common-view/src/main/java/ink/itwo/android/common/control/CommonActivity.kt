package ink.itwo.android.common.control

import android.os.Bundle
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity

/** Created by wang on 2020/3/26. */
open class CommonActivity : SwipeBackActivity() {
    protected var mActivity: CommonActivity? = null
    /** 是否支持滑动返回  */
    protected var hasSwipeBackEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        setSwipeBackEnable(false)
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        super.setSwipeBackEnable(enable)
        hasSwipeBackEnable = enable
    }
}
