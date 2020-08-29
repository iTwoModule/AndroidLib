package ink.itwo.android.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ink.itwo.android.common.CommonUtil
import ink.itwo.android.common.CommonUtil.Companion.jsonStr
import ink.itwo.android.common.ktx.TIME_PATTERN
import ink.itwo.android.common.ktx.log
import ink.itwo.android.common.ktx.toStr
import ink.itwo.android.http.NetManager
import ink.itwo.android.http.file.DownLoadInfo
import ink.itwo.android.http.ktx.executorCoroutineDispatcher
import ink.itwo.android.http.ktx.launchIO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledThreadPoolExecutor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView?.setOnClickListener {
//            startActivity(Intent(this, SecondActivity::class.java))
//         aa()
            down()
        }
    }

    private fun aa() {
        launchIO {
            User().apply {
                id = 1
                age = 20
                nickName = "abc 123"
            }.jsonStr().log()
        }
    }

    var urls = mutableListOf<String>().apply {
        add("http://files.itwo.ink/apk/fd0c3a82-0ec3-49ae-8eb4-f5c2350557f3.apk")
        add("http://files.itwo.ink/apk/d026f158-caa9-4f39-a4b7-fdc314b2f597.apk")
        add("http://files.itwo.ink/apk/6aef88fe-d209-48e2-9aef-e264a8c43e1a.apk")
        add("http://files.itwo.ink/apk/45b93681-7f0d-4ac4-8b23-16e081662642.apk")
        add("http://files.itwo.ink/apk/icon/1/ic_launcher.png")
        add("http://files.itwo.ink/apk/icon/1/ic_launcher_1.png")
        add("http://files.itwo.ink/apk/icon/1/ic_launcher_2.png")
        add("http://files.itwo.ink/apk/icon/1/ic_launcher_3.png")
        add("http://files.itwo.ink/apk/icon/1/ic_launcher_4.png")
        add("http://files.itwo.ink/apk/icon/1/ic_launcher_5.png")
        add("http://files.itwo.ink/apk/icon/1/ic_launcher_6.png")
        add("http://files.itwo.ink/apk/icon/1/ic_launcher_7.png")
        add("http://files.itwo.ink/apk/icon/1/ic_launcher_8.png")
        add("http://files.itwo.ink/apk/icon/1/ic_launcher_9.png")
        add("http://files.itwo.ink/apk/icon/1/ic_launcher_10.png")
    }

    private fun down() {
        Date().toStr(TIME_PATTERN).log()

        lifecycleScope.launch(executorCoroutineDispatcher){
            val infoList = urls.mapIndexed { index, s ->
              DownLoadInfo(url = s, progressListener = { c, a, b ->
//                    "$index  bytesRead $c  contentLength $a  done $b   threadId ${Thread.currentThread().id}".log()
                })
            }.toMutableList()
            val resultList = NetManager.down.downMulti(infoList)
            resultList.jsonStr().log()
            Date().toStr(TIME_PATTERN).log()
        }
    }
}
