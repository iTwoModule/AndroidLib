package ink.itwo.android.common

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import ink.itwo.android.common.Common.context

/** Created by wang on 1/24/21. */


//  context
val Common.packageInfo: PackageInfo? by lazy { context?.packageName?.let { ActivityStack.instance.get()?.packageManager?.getPackageInfo(it, PackageManager.GET_CONFIGURATIONS) } }

val Common.versionName by lazy { Common.packageInfo?.versionName }
val Common.versionCode by lazy { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) Common.packageInfo?.longVersionCode else Common.packageInfo?.versionCode?.toLong() }
fun Common.toPlayStore(applicationId: String) {
    try {
        val uri = Uri.parse("market://details?id=${applicationId}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityCurrent?.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
val String.sssaa
   get() = aaaa(this)

fun aaaa(aa:String): String {
    return  ""
}