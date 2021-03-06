package ink.itwo.android.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import ink.itwo.android.common.jsonStr
import ink.itwo.android.common.TIME_PATTERN
import ink.itwo.android.common.ktx.log
import ink.itwo.android.common.toStr
import ink.itwo.android.coroutines.NetManager
import ink.itwo.android.coroutines.file.DownLoadInfo
import ink.itwo.android.coroutines.file.UploadInfo
import ink.itwo.android.coroutines.ktx.launch
import ink.itwo.android.media.picker.compress
import ink.itwo.android.media.picker.pickImage
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

/** Created by wang on 2020/8/21. */
class SecondActivity : AppCompatActivity() /*CoroutineScope by MainScope()  , */ {
    val viewModel: SecondViewModel by lazy { ViewModelProvider(this).get(SecondViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

    }


    private fun imagePick() {
        launch {
            val paths = pickImage(3).compress()?.map { it?.path }
            var url = "http://apk.itwo.ink/up_multi"
            val uploadInfo = UploadInfo(url = url, key = UUID.randomUUID().toString())
            paths?.forEach { path -> path?.let { uploadInfo.addFile(File(it)) } }
            val result = NetManager.file.up(uploadInfo)
            result.jsonStr().log()
        }
    }

    private fun up() {
        Date().toStr(TIME_PATTERN).log()
        var url = "http://apk.itwo.ink/up_multi"
        var path0 = this.externalCacheDir?.path + "/" + "ic_launcher.png"
        var path1 = this.externalCacheDir?.path + "/" + "ic_launcher_1.png"
        var paths = arrayOf(path0, path1)
        lifecycleScope.launch {
            var infoList = paths.mapIndexed { index, it ->
                UploadInfo(url, UUID.randomUUID().toString()) { a, b, c ->
                    "$index  bytesRead $c  contentLength $a  done $b   threadId ${Thread.currentThread().id}".log()
                }.addFile(File(it)).addParams("name", "name1").addParams("age", 1)
            }.toMutableList()
            val upMulti = NetManager.file.upMulti(infoList)
            upMulti.jsonStr().log()
        }
        lifecycleScope.launch {
            var result = NetManager.file.up(UploadInfo(url, key = UUID.randomUUID().toString()) { a, b, c ->
                "  bytesRead $c  contentLength $a  done $b   threadId ${Thread.currentThread().id}".log()
            }.addFile(File(path0)))
            result.jsonStr().log()
        }
    }

    var urls = mutableListOf<String>().apply {
        add("http://files.itwo.ink/apk/fd0c3a82-0ec3-49ae-8eb4-f5c2350557f3.apk")
        add("http://files.itwo.ink/apk/d026f158-caa9-4f39-a4b7-fdc314b2f597.apk")
        add("http://files.itwo.ink/apk/6aef88fe-d209-48e2-9aef-e264a8c43e1a.apk")
//        add("http://files.itwo.ink/apk/45b93681-7f0d-4ac4-8b23-16e081662642.apk")
//        add("http://files.itwo.ink/apk/icon/1/ic_launcher.png")
//        add("http://files.itwo.ink/apk/icon/1/ic_launcher_1.png")
//        add("http://files.itwo.ink/apk/icon/1/ic_launcher_2.png")
//        add("http://files.itwo.ink/apk/icon/1/ic_launcher_3.png")
//        add("http://files.itwo.ink/apk/icon/1/ic_launcher_4.png")
//        add("http://files.itwo.ink/apk/icon/1/ic_launcher_5.png")
//        add("http://files.itwo.ink/apk/icon/1/ic_launcher_6.png")
//        add("http://files.itwo.ink/apk/icon/1/ic_launcher_7.png")
//        add("http://files.itwo.ink/apk/icon/1/ic_launcher_8.png")
//        add("http://files.itwo.ink/apk/icon/1/ic_launcher_9.png")
//        add("http://files.itwo.ink/apk/icon/1/ic_launcher_10.png")
    }

    private fun down() {
        Date().toStr(TIME_PATTERN).log()

        lifecycleScope.launch {
            val infoList = urls.mapIndexed { index, s ->
                DownLoadInfo(url = s, progressListener = { c, a, b ->
                    "$index  bytesRead $c  contentLength $a  done $b   threadId ${Thread.currentThread().id}".log()
                })
            }.toMutableList()
            val resultList = NetManager.file.downMulti(infoList)
            resultList.jsonStr().log()
            Date().toStr(TIME_PATTERN).log()
        }

        /*lifecycleScope.launch {
            (Looper.getMainLooper() == Looper.myLooper()).log()
            Thread.currentThread().name.log()
            var result = NetManager.down.down(DownLoadInfo(url = urls[0]))
            result.jsonStr().log()
        }*/
    }
}


