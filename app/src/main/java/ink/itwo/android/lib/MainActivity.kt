package ink.itwo.android.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ink.itwo.android.common.jsonStr
import ink.itwo.android.common.ktx.log
import ink.itwo.android.common.unicode
import ink.itwo.android.coroutines.ktx.io
import ink.itwo.android.coroutines.ktx.launch
import kotlinx.android.synthetic.main.activity_main.*
import okio.utf8Size

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
            val result = io { api.userInfoSuccess() }
            result.data?.jsonStr().log()
            result.data?.jsonStr().log()
            result.data?.jsonStr().log()
            result.data?.jsonStr().log()
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
            val i = 1 / 0
            i.log()
        }
    }


}
