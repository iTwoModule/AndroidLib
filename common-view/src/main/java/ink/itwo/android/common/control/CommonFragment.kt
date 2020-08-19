package ink.itwo.android.common.control

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment

/** Created by wang on 2020/3/26. */
open abstract class CommonFragment<MActivity : CommonActivity> : SwipeBackFragment() {
    protected lateinit var mActivity: MActivity
    /** inflater View  */
    @JvmField
    protected var viewRoot: View? = null
    /** 是否支持滑动返回  */
    @JvmField
    protected var hasSwipeBackEnable = true

    abstract val layoutId:Int

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity as MActivity
    }

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        if (viewRoot != null) {
            val parent = viewRoot?.parent as? ViewGroup
            parent?.removeView(viewRoot)
            viewRoot?.let { onCreateView(it) }
            return viewRoot as View
        }
        viewRoot = inflater.inflate(layoutId, container, false)
        viewRoot?.let { onCreateView(it) }
        return if (hasSwipeBackEnable) attachToSwipeBack(viewRoot) else viewRoot as View
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        super.setSwipeBackEnable(enable)
        hasSwipeBackEnable = enable
    }

    /** 可以在这里初始化 View  */
    open fun onCreateView(viewRoot: View) {}

}