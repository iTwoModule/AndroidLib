package ink.itwo.android.http.permissions

import android.os.Bundle
import androidx.fragment.app.Fragment

/** Created by wang on 2020/8/19. */
class PermissionsFm : Fragment() {
    private val REQUEST_CODE=0x96
    var listener:((MutableList<String>?,IntArray?)->Unit)?=null

    fun request(vararg permissions: String) {
        requestPermissions(permissions,REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (REQUEST_CODE == requestCode) {
            listener?.invoke(permissions.toMutableList(),grantResults)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PermissionsFm().apply {
                arguments = Bundle().apply {
                }
            }
    }
}