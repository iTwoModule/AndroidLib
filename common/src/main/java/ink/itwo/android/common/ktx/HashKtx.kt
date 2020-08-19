package ink.itwo.android.common.ktx

import java.nio.charset.Charset
import java.security.MessageDigest

/** Created by wang on 2020/5/6. */


private fun hash(data: ByteArray, algorithm: Hash): ByteArray {
    val messageDigest = MessageDigest.getInstance(algorithm.name)
    return messageDigest.digest(data)
}

fun ByteArray.hash(algorithm: Hash): String {
    return hash(this, algorithm).toHexString()
}

fun String.hash(algorithm: Hash, charset: Charset = Charset.forName("utf-8")): String {
    return toByteArray(charset).hash(algorithm)
}

val String.md5Encode: String get() = this.md5()
val String.sha1Encode: String get() = this.sha1()

fun ByteArray.md5Bytes(): ByteArray = hash(this, Hash.MD5)
fun ByteArray.md5(): String = hash(this, Hash.MD5).toHexString()
fun String.md5(charset: Charset = Charset.forName("utf-8")): String = toByteArray(charset).md5()
fun ByteArray.sha1Bytes(): ByteArray = hash(this, Hash.SHA1)
fun ByteArray.sha1(): String = hash(this, Hash.SHA1).toHexString()
fun String.sha1(charset: Charset = Charset.forName("utf-8")): String = toByteArray(charset).sha1()
fun ByteArray.sha224Bytes(): ByteArray = hash(this, Hash.SHA224)
fun ByteArray.sha224(): String = hash(this, Hash.SHA224).toHexString()
fun String.sha224(charset: Charset = Charset.forName("utf-8")): String = toByteArray(charset).sha224()
fun ByteArray.sha256Bytes(): ByteArray = hash(this, Hash.SHA256)
fun ByteArray.sha256(): String = hash(this, Hash.SHA256).toHexString()
fun String.sha256(charset: Charset = Charset.forName("utf-8")): String = toByteArray(charset).sha256()
fun ByteArray.sha384Bytes(): ByteArray = hash(this, Hash.SHA384)
fun ByteArray.sha384(): String = hash(this, Hash.SHA384).toHexString()
fun String.sha384(charset: Charset = Charset.forName("utf-8")): String = toByteArray(charset).sha384()
fun ByteArray.sha512Bytes(): ByteArray = hash(this, Hash.SHA512)
fun ByteArray.sha512(): String = hash(this, Hash.SHA512).toHexString()
fun String.sha512(charset: Charset = Charset.forName("utf-8")): String = toByteArray(charset).sha512()



enum class Hash {
    MD5,
    SHA1,
    SHA224,
    SHA256,
    SHA384,
    SHA512,
}

fun ByteArray.toHexString(): String {
    val result = CharArray(size shl 1)
    var index = 0
    for (b in this) {
        result[index++] = HEX_DIGITS[b.toInt().shr(4) and 0xf]
        result[index++] = HEX_DIGITS[b.toInt() and 0xf]
    }
    return String(result)
}

private val HEX_DIGITS =
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
