package ink.itwo.android.common

import java.math.BigDecimal
import java.util.regex.Pattern

/** Created by wang on 1/24/21. */


fun Double?.formatDoubleToDecimal(newScale: Int = 2, roundingMode: Int = BigDecimal.ROUND_HALF_UP): String? {
    return this?.let { BigDecimal(it).setScale(newScale, roundingMode).toString() }
}

fun String?.formatStringToDecimal(newScale: Int = 2, roundingMode: Int = BigDecimal.ROUND_HALF_UP): String? {
    return BigDecimal(this).setScale(newScale, roundingMode).toString()
}

fun String?.hideMobile(): String? {
    if (this.isNullOrBlank()) return ""
    var cs = this.toCharArray()
    cs[3] = '*'
    cs[4] = '*'
    cs[5] = '*'
    cs[6] = '*'
    return String(cs)
}

val String.unicode
    get() = this.unicode()
val String.unicode2
    get() = this.unicode2()
val String.unicodeToStr
    get() = this.unicodeToString()
val String.upperInitial
    get() = this.toUpperInitial()

/** 把中文字符串转换为十六进制Unicode编码字符串  */
private fun String.unicode(): String {
    var str = ""
    for (element in this) {
        val ch = element.toInt()
        str += if (ch > 255) "\\u" + Integer.toHexString(ch) else "\\" + Integer.toHexString(ch)
    }
    return str
}

/** 字符串转换unicode  */
private fun String.unicode2(): String {
    val unicode = StringBuffer()
    for (element in this) {
        // 转换为unicode
        unicode.append("\\u" + Integer.toHexString(element.toInt()))
    }
    return unicode.toString()
}

/** 把十六进制Unicode编码字符串转换为中文字符串  */
private  fun String.unicodeToString(): String {
    var str1 = this
    val pattern = Pattern.compile("(\\\\u(\\p{XDigit}{2,4}))")
    val matcher = pattern.matcher(str1)
    var ch: Char
    while (matcher.find()) {
        ch = matcher.group(2).toInt(16).toChar()
        str1 = str1.replace(matcher.group(1), ch.toString() + "")
    }
    return str1
}

/** unicode 转字符串  */
private  fun String.unicode2String(): String {
    val string = StringBuffer()
    val hex = this.split("\\\\u").toTypedArray()
    for (i in 1 until hex.size) { // 转换出每一个代码点
        val data = hex[i].toInt(16)
        // 追加成string
        string.append(data.toChar())
    }
    return string.toString()
}

/**
 * 将byte[]转换成16进制字符串
 * @param data 要转换成字符串的字节数组
 * @return 16进制字符串
 */
private fun printHexString(data: ByteArray): String? {
    val s = StringBuffer()
    for (i in data.indices) {
        var hex = Integer.toHexString(data[i].toInt() and 0xFF)
        if (hex.length == 1) {
            hex = "0$hex"
        }
        s.append(hex)
    }
    return s.toString()
}

/** 首字母变大写*/
private fun String?.toUpperInitial(): String {
    if (this.isNullOrEmpty()) return ""
    var charArray = this.toCharArray()
    if (Character.isLowerCase(charArray[0])) {
        var c = charArray[0]
        charArray[0] = (c.toInt() - 32).toChar()
    }
    return String(charArray)
}

