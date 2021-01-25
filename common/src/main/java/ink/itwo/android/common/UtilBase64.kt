package ink.itwo.android.common

import android.util.Base64
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/** Created by wang on 2019-11-09. */
object UtilBase64 {

    /** 将文件转成base64 字符串*/
    @Throws(Exception::class)
    fun encodeBase64File(path: String): String {
        val file = File(path)
        val inputFile = FileInputStream(file)
        val buffer = ByteArray(file.length().toInt())
        inputFile.read(buffer)
        inputFile.close()
        return Base64.encodeToString(buffer, Base64.DEFAULT)
    }

    /**
     * 将base64字符保存文本文件
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    @Throws(Exception::class)
    fun toFile(base64Code: String, targetPath: String) {
        val buffer = Base64.decode(base64Code, Base64.NO_WRAP)
        val out = FileOutputStream(targetPath)
        out.write(buffer)
        out.close()
    }
}