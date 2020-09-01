package ink.itwo.android.media.picker

import androidx.fragment.app.FragmentActivity
import com.ypx.imagepicker.ImagePicker
import com.ypx.imagepicker.bean.ImageItem
import com.ypx.imagepicker.bean.MimeType
import com.ypx.imagepicker.builder.MultiPickerBuilder
import ink.itwo.android.common.ActivityStack
import ink.itwo.android.common.ktx.copyAndConvert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.zibin.luban.Luban
import java.io.File
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/** Created by wang on 2020/9/1. */


/*
ImagePicker.withMulti(new WeChatPresenter()) //指定presenter
                .setMaxCount(count)//设置选择的最大数
                .setColumnCount(4)
                .mimeTypes(com.ypx.imagepicker.bean.MimeType.ofImage())
                .showCamera(true) //显示拍照
                .setPreview(true) //开启预览
//                .setSinglePickImageOrVideoType(true) //当单选或者视频单选时，点击item直接回调，无需点击完成按钮
                .setSinglePickWithAutoComplete(false) //显示原图
                .setOriginal(true) //显示原图时默认原图选项开关
                .setDefaultOriginal(false) //设置单选模式，当maxCount==1时，可执行单选（下次选中会取消上一次选中）
*/
suspend fun MultiPickerBuilder.pick(): ArrayList<ImageItem> =
        suspendCoroutine { c ->
            var activity = ActivityStack.instance.get()
            pick(activity) { c.resume(it) }
        }

/** 选择图片 - 压缩 - 复制到手机的沙盒目录*/
suspend fun MultiPickerBuilder.compress(): MutableList<File?>? {
    var activity = ActivityStack.instance.get() as FragmentActivity
    var list = this.pick()
    val map = list.map {
        var extension = it.mimeType.split("/").lastOrNull() ?: "jpg"
        var sandboxPath = (activity.getExternalFilesDir(null)?.absolutePath ?: "") + File.separator + UUID.randomUUID().toString() + "." + extension
        it.uri.copyAndConvert(activity, sandboxPath)
    }
    var files = withContext(Dispatchers.IO) {  Luban.with(activity).load(map).get() }
    return suspendCoroutine { it.resume(files) }
}

fun pickImage(maxCount:Int): MultiPickerBuilder =ImagePicker.withMulti(WeChatPresenter()).mimeTypes(MimeType.ofImage()).setMaxCount(maxCount)

