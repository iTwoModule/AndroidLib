package ink.itwo.android.http

import android.content.Context
import ink.itwo.android.http.file.DownLoadManager

/** Created by wang on 2020/8/29. */
object NetManager {
    var base_url:String?=null
    var context:Context?=null
    fun init(context: Context,config: Config) {
        base_url=config.root_url
        this.context=context
        HttpManager.instance.init(config)
    }
    val http=HttpManager.instance
    val down=DownLoadManager.instance
}