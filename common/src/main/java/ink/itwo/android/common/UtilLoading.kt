package ink.itwo.android.common

import android.app.Dialog

/** Created by wang on 2/1/21. */

val dialog: Dialog? by Common.context?.let {
    lazy {
        object : Dialog(it) {

        }
    }
}

fun loadingShow(): Boolean {
    if (dialog?.isShowing != true) {
        dialog?.show()
        return true
    }
    return false
}

fun loadingDismiss() {
    if (dialog?.isShowing == true) {
        dialog?.dismiss()
    }
}