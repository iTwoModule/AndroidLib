package ink.itwo.android.skeleton

import android.view.View
import androidx.annotation.LayoutRes

/** Created by wang on 2020/9/25. */
interface Skeleton<T:View> {
    fun bind(v: T):Skeleton<T>
    fun load(@LayoutRes layoutId:Int):Skeleton<T>
    fun show():Skeleton<T>
    fun hide()
}