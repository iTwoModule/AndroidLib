package ink.itwo.android.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ink.itwo.android.common.jsonStr
import ink.itwo.android.common.ktx.log
import ink.itwo.android.common.mimeType
import ink.itwo.android.common.unicode
import ink.itwo.android.coroutines.file.HttpFileManager
import ink.itwo.android.coroutines.file.UploadInfo
import ink.itwo.android.coroutines.ktx.*
import ink.itwo.android.coroutines.permissions.requestCoroutinesPermissions
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvSuccess?.setOnClickListener { success() }
        tvError?.setOnClickListener { error() }
        tvNetWordError?.setOnClickListener { netWordError() }
        tvLaunch?.setOnClickListener { testLaunch() }
        "".unicode
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
        launch {
            val requestCoroutinesPermissions = requestCoroutinesPermissions(this, android.Manifest.permission.CAMERA, android.Manifest.permission.FACTORY_TEST)
        }
    }


}
