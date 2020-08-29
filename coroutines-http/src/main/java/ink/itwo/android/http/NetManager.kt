package ink.itwo.android.http

import android.content.Context
import ink.itwo.android.http.file.DownLoadManager
import kotlinx.coroutines.ExecutorCoroutineDispatcher

/** Created by wang on 2020/8/29. */
object NetManager {
    var base_url:String?=null
    var context:Context?=null
    lateinit var executorCoroutineDispatcher:ExecutorCoroutineDispatcher
    fun init(context: Context,config: Config,executorCoroutineDispatcher: ExecutorCoroutineDispatcher) {
        base_url=config.root_url
        this.context=context
        this.executorCoroutineDispatcher=executorCoroutineDispatcher
        HttpManager.instance.init(config)
    }
    val http=HttpManager.instance
    val down=DownLoadManager.instance
}