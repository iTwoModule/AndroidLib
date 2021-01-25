package ink.itwo.android.common

import android.content.Context
import android.view.View
import android.widget.ImageView
import java.io.Serializable

/** Created by wang on 1/24/21. */



interface ImageLoader : Serializable {
    fun displayImage(activity: Context?, path: Any?, imageView: ImageView?, width: Int, height: Int)

    fun displayImage(context: Context?, url: Any?, imageView: ImageView?)

    fun clearMemoryCache()
}

typealias  ViewAction= (View)->Unit