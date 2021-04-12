package ink.itwo.android.lib

import android.Manifest.permission.SYSTEM_ALERT_WINDOW
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.util.Util
import ink.itwo.android.common.Common
import ink.itwo.android.common.deviceWith
import ink.itwo.android.common.jsonStr
import ink.itwo.android.common.ktx.log
import ink.itwo.android.common.unicode
import ink.itwo.android.coroutines.ktx.io
import ink.itwo.android.coroutines.ktx.launch
import ink.itwo.android.coroutines.permissions.requestCoroutinesPermissions
import ink.itwo.android.debug.stack.DebugStackHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvSuccess?.setOnClickListener { success() }
        tvError?.setOnClickListener { testLaunch() }
        tvNetWordError?.setOnClickListener {
            var intent =  Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()))
            startActivity(intent)
        }
//        tvLaunch?.setOnClickListener { testLaunch() }w
        tvLaunch?.setOnClickListener {
            val decorView = (this@MainActivity).window.decorView
            if (decorView != null) {
                val layout = decorView.findViewById<View>(R.id.layout)
               var decorViewParent =decorView.parent
                "$decorView".log()
            }
//            launch {
//                val requestCoroutinesPermissions = requestCoroutinesPermissions(this, SYSTEM_ALERT_WINDOW)
//                DebugStackHelper.show()
//            }
        }
        "".unicode
        Common.deviceWith
    }

    fun success() {
        launch {
            val result = io { api.userInfoSuccess() }.take()
        }
    }

    fun error() {
        launch() {
            val result = io { api.userInfoError() }.take()
            result?.jsonStr()?.log()
            result?.jsonStr()?.log()
            result?.jsonStr()?.log()
            result?.jsonStr()?.log()

        }
        /*dsl {
            block {
                io {  api.userInfoError()}.take()
            }
        }*/
    }

    fun netWordError() {
        launch {
            val user = io { api.netWorkError() }.take()
            user?.jsonStr()?.log()
            user?.jsonStr()?.log()
            user?.jsonStr()?.log()
            user?.jsonStr()?.log()
        }
    }

    fun testLaunch() {
        startActivity(Intent(this,SecondActivity::class.java))
//        launch {
//            val requestCoroutinesPermissions = requestCoroutinesPermissions(this, android.Manifest.permission.CAMERA, android.Manifest.permission.FACTORY_TEST)
//        }
    }


}
