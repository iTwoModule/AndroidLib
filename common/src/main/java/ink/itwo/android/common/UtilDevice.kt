package ink.itwo.android.common

import android.util.DisplayMetrics
import androidx.annotation.DimenRes

/** Created by wang on 1/24/21. */


//device
private val Common.dm: DisplayMetrics by lazy { DisplayMetrics().apply { ActivityStack.instance.get()?.windowManager?.defaultDisplay?.getRealMetrics(this) } }

/** 屏幕宽度*/
val Common.deviceWith: Int by lazy { Common.dm.widthPixels }

/** 屏幕高度*/
val Common.deviceHeight: Int by lazy { Common.dm.heightPixels }

/** dp 换 px*/
fun Common.toPx(dpValue: Float): Int {
    val scale = context?.resources?.displayMetrics?.density ?: 0F
    return (dpValue * scale + 0.5f).toInt()
}

/** px 换 dp*/
fun Common.toDp(pxValue: Float): Int {
    val scale = context?.resources?.displayMetrics?.density ?: 0F
    return (pxValue / scale + 0.5f).toInt()
}

/** 从Dimension中转换成PX  */
fun Common.dimenToPx(@DimenRes id: Int): Int {
    return context?.resources?.getDimension(id)?.toInt() ?: 0
}

/** 从Dimension中转换成DP  */
fun Common.dimenToDp(@DimenRes id: Int): Int {
    return toDp(context?.resources?.getDimension(id) ?: 0F)
}
