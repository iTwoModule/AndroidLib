package ink.itwo.android.common.ktx

import java.math.BigDecimal
import java.util.regex.Pattern

/** Created by wang on 2020/3/26. */

object StringKtx {


    fun Double?.formatDecimal(newScale: Int = 2, roundingMode: Int = BigDecimal.ROUND_HALF_UP): String? {
        return getFormatDecimal(this, newScale, roundingMode)
    }

    fun String?.formatDecimal(newScale: Int = 2, roundingMode: Int = BigDecimal.ROUND_HALF_UP): String? {
        return getFormatDecimal(this, newScale, roundingMode)
    }

    fun String?.hideMobile(): String? {
        return getHideMobile(this)
    }

    @JvmStatic
    fun getFormatDecimal(s: String?, newScale: Int = 2, roundingMode: Int = BigDecimal.ROUND_HALF_UP): String? {
        return s?.let { BigDecimal(it).setScale(newScale, roundingMode).toString() }
    }

    @JvmStatic
    fun getFormatDecimal(d: Double?, newScale: Int = 2, roundingMode: Int = BigDecimal.ROUND_HALF_UP): String? {
        return d?.let { BigDecimal(it).setScale(newScale, roundingMode) }?.toString()
    }

    @JvmStatic
    fun getHideMobile(mobile: String?): String {
        if (mobile.isNullOrBlank()) return ""
        var cs = mobile.toCharArray()
        cs[3] = '*'
        cs[4] = '*'
        cs[5] = '*'
        cs[6] = '*'
        return String(cs)
    }

    /** 把中文字符串转换为十六进制Unicode编码字符串  */
    @JvmStatic
    fun stringToUnicode(s: String): String {
        var str = ""
        for (element in s) {
            val ch = element.toInt()
            str += if (ch > 255) "\\u" + Integer.toHexString(ch) else "\\" + Integer.toHexString(ch)
        }
        return str
    }

    /** 字符串转换unicode  */
    fun stringToUnicode2(string: String): String {
        val unicode = StringBuffer()
        for (element in string) {
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(element.toInt()))
        }
        return unicode.toString()
    }

    /** 把十六进制Unicode编码字符串转换为中文字符串  */
    fun unicodeToString(str: String): String {
        var str = str
        val pattern = Pattern.compile("(\\\\u(\\p{XDigit}{2,4}))")
        val matcher = pattern.matcher(str)
        var ch: Char
        while (matcher.find()) {
            ch = matcher.group(2).toInt(16).toChar()
            str = str.replace(matcher.group(1), ch.toString() + "")
        }
        return str
    }

    /** unicode 转字符串  */
    fun unicode2String(unicode: String): String {
        val string = StringBuffer()
        val hex = unicode.split("\\\\u").toTypedArray()
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
    fun String?.toUpperInitial():String {
        if (this.isNullOrEmpty()) return ""
        var charArray = this.toCharArray()
        if (Character.isLowerCase(charArray[0])) {
            var c = charArray[0]
            charArray[0] = (c.toInt() - 32).toChar()
        }
        return String(charArray)
    }

}
