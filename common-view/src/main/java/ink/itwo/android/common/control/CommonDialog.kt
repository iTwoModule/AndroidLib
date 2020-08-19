package ink.itwo.android.common.control

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.annotation.Nullable
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/** Created by wang on 2020/3/26. */

open abstract class CommonDialog<MActivity : Context> : DialogFragment() {
    protected var fullscreen = false //是否全屏无状态栏
    protected var margin = 0 //左右边距
    protected var width = 0 //宽度
    protected var height = 0 //高度
    protected var dimAmount = 0.5f //灰度深浅
    protected var gravity = Gravity.CENTER
    protected var outCancel = true //是否点击外部取消
    @StyleRes protected var animStyle = 0
    abstract var layoutId: Int
    protected var mActivity: MActivity? = null
    abstract fun convertView(view: View?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CommonDialog)
        //恢复保存的数据
        if (savedInstanceState != null) {
            margin = savedInstanceState.getInt(MARGIN)
            width = savedInstanceState.getInt(WIDTH)
            height = savedInstanceState.getInt(HEIGHT)
            dimAmount = savedInstanceState.getFloat(DIM)
//            showBottom = savedInstanceState.getBoolean(BOTTOM)
            gravity = savedInstanceState.getInt(GRAVITY)
            fullscreen = savedInstanceState.getBoolean(FULLSCREEN)
            outCancel = savedInstanceState.getBoolean(CANCEL)
            animStyle = savedInstanceState.getInt(ANIM)
            layoutId = savedInstanceState.getInt(LAYOUT)
        }
    }


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity as MActivity
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        val view = inflater.inflate(layoutId, container, false)
        convertView(view)
        return view
    }

    override fun onStart() {
        super.onStart()
        initParams()
    }

    /**屏幕旋转等导致DialogFragment销毁后重建时保存数据*/
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MARGIN, margin)
        outState.putInt(WIDTH, width)
        outState.putInt(HEIGHT, height)
        outState.putFloat(DIM, dimAmount)
        outState.putInt(GRAVITY, gravity)
        outState.putBoolean(CANCEL, outCancel)
        outState.putBoolean(FULLSCREEN, fullscreen)
        outState.putInt(ANIM, animStyle)
        outState.putInt(LAYOUT, layoutId)
    }

    protected fun initParams() {
        val window: Window? = dialog?.window
        if (window != null) {
            val lp = window.attributes
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount

            lp.gravity = gravity

            //设置dialog宽度
            when (width) {
                MATCH_PARENT -> {
                    lp.width = context?.resources?.displayMetrics?.widthPixels ?: 0 - 2 * margin
                }
                WRAP_CONTENT -> {
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                }
                else -> {
                    lp.width = width
                }
            }
            //设置dialog高度
            if (height == WRAP_CONTENT) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            } else {
                lp.height = height
            }

            initParams(lp)

            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle)
            window.attributes = lp
            if (fullscreen) {
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
        isCancelable = outCancel
    }
    protected open fun initParams(lp:WindowManager.LayoutParams){

    }



    fun show(manager: FragmentManager): CommonDialog<*> {
        val ft: FragmentTransaction = manager.beginTransaction()
        if (this.isAdded) {
            ft.remove(this).commit()
        }
        ft.add(this, System.currentTimeMillis().toString())
        ft.commitAllowingStateLoss()
        return this
    }

    companion object {
        const val MATCH_PARENT = -1
        const val WRAP_CONTENT = -2
        protected const val MARGIN = "margin"
        protected const val WIDTH = "width"
        protected const val HEIGHT = "height"
        protected const val DIM = "dim_amount"
        protected const val GRAVITY = "gravity"
        protected const val CANCEL = "out_cancel"
        protected const val ANIM = "anim_style"
        protected const val LAYOUT = "layout_id"
        protected const val FULLSCREEN = "fullscreen"
    }
}