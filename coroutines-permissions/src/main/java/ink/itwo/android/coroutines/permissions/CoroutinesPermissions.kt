package ink.itwo.android.coroutines.permissions

import ink.itwo.android.common.ActivityStack
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/** Created by wang on 2020/8/19. */
class CoroutinesPermissions {
    private val TAG_FM = "TAG_PermissionsFm"
    var listener: ((Boolean, MutableList<String>) -> Unit)? = null

    fun request(vararg permissions: String) {
        val fragmentActivity = ActivityStack.instance.get()
        var manager=fragmentActivity?.supportFragmentManager
        var fm = (manager?.findFragmentByTag(TAG_FM) as? PermissionsFm)
        if (fm == null) {
            fm = PermissionsFm.newInstance()
            fm.listener = { permissionsRequest, grantResults ->
                //0代表该权限请求成功，-1代表失败
                var result = grantResults?.any { it==-1 } != true
                var list = mutableListOf<String>()

                grantResults?.forEachIndexed { index, i ->
                    if (i == -1) {
                        permissionsRequest?.get(index)?.let {
                            list.add(it)
                        }
                    }
                }
                manager?.beginTransaction()?.remove(fm)?.commitAllowingStateLoss()
                listener?.invoke(result, list)
            }
            manager?.beginTransaction()?.add(fm, TAG_FM)?.commitAllowingStateLoss()
            manager?.executePendingTransactions()
        }
        fm.request(*permissions)
    }
}
/** 请求权限 first=true 同意， first=false 拒绝 second 拒绝的权限列表*/
suspend fun requestCoroutinesPermissions(vararg permissions: String): Pair<Boolean, MutableList<String>> =
        suspendCoroutine {
            val coroutinesPermissions = CoroutinesPermissions()
            coroutinesPermissions.listener = { b, list ->
                it.resume(b to list)
            }
            coroutinesPermissions.request(*permissions)
        }
