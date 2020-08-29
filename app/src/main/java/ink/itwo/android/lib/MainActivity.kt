package ink.itwo.android.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ink.itwo.android.common.CommonUtil.Companion.jsonStr
import ink.itwo.android.common.ktx.log
import ink.itwo.android.http.NetManager
import ink.itwo.android.http.file.DownLoadInfo
import ink.itwo.android.http.ktx.launchIO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

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


    private fun down() {
        GlobalScope.launch {
            val withContext = withContext(Dispatchers.IO) {
                NetManager.down.down(DownLoadInfo(url = ""))

            }
        }
        launchIO(exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.log()
            throwable.log()
            throwable.log()
        }) {
//            var url="http://files.itwo.ink/apk/f48a04dc-7888-4cb9-9cc5-3de46055cc2f.apk"

            var urls = mutableListOf<String>().apply {
                add("http://files.itwo.ink/apk/fd0c3a82-0ec3-49ae-8eb4-f5c2350557f3.apk")
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
            val infoList = urls.mapIndexed { index, s ->
                DownLoadInfo(url = s, progressListener = { c, a, b ->
                    "$index  bytesRead $c  contentLength $a  done $b".log()
                })
            }.toMutableList()
            val map = NetManager.down.downMulti(infoList)
            map.jsonStr().log()
        }
    }
}
