package ink.itwo.android.common

import android.view.Gravity
import android.view.View
import android.widget.Toast

/** Created by wang on 1/24/21. */
//toast
private var sToast: Toast? = Toast.makeText(Common.context, "", Toast.LENGTH_LONG)

//private var sToast:Toast?=null
private val defaultYOffset = sToast?.yOffset
fun String?.toast(gravity: Int = Gravity.BOTTOM, xOffset: Int = 0, yOffset: Int = defaultYOffset ?: 0, view: View? = null) {
    if (sToast == null) sToast = Toast.makeText(Common.context, null, Toast.LENGTH_LONG)
    else {
        sToast?.cancel()
        sToast = Toast.makeText(Common.context, "", Toast.LENGTH_LONG)
//        sToast?.setText(this)
    }
    sToast?.setText(this)
    sToast?.setGravity(gravity, xOffset, yOffset)
    view?.let { sToast?.view = view }
    sToast?.show()
}
