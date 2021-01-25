package ink.itwo.android.common

import java.text.SimpleDateFormat
import java.util.*

/** Created by wang on 2020/5/6. */
const val TIME_DAY_MILLISECOND: Int = 86400000
const val DATE_PATTERN = "yyyy-MM-dd"
const val TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"
/** date to string*/
fun Date?.toStr(pattern: String?= DATE_PATTERN, local: Locale?= Locale.getDefault()): String?=try { SimpleDateFormat(pattern,local ).format(this) }catch (e:Exception){null}
/** string to date*/
fun String?.toDate(pattern: String?= DATE_PATTERN): Date?=try{ SimpleDateFormat(pattern).parse(this)}catch (e:Exception){null}
/** 时间戳 to str*/
fun Long?.toDateStr(pattern: String? = DATE_PATTERN): String?=this?.let { try{ SimpleDateFormat(pattern).format(Date(it))}catch (e:Exception){null}}?:null

/** 得到本日的上月时间 如果当日为2007-9-1,那么获得2007-8-1  */
fun Date?.getDateBeforeMonth(): Date? {
    val cal = Calendar.getInstance()
    cal.add(Calendar.MONTH, -1)
    return cal.time
}

/** 得到本日的前几个月时间 如果number=2当日为2007-9-1,那么获得2007-7-1  */
fun Date?.getDateBeforeMonth(number: Int): Date? {
    return this?.let {
        val cal = Calendar.getInstance()
        cal.time = it
        cal.add(Calendar.MONTH, -number)
        return cal.time
    }
}
/** @param iDate 如果要获得前几天日期，该参数为负数； 如果要获得后几天日期，该参数为正数*/
fun Date?.getBeforeOrAfter(iDate: Int, field: Int = Calendar.DAY_OF_MONTH): Date? {
    return this?.let {
        val cal = Calendar.getInstance()
        cal.time = it
        cal.add(field, iDate)
        return cal.time
    }
}
/** 指定日期的月的第一天  */
fun Date?.getFirstDayOfMonth(): Date? {
    return  this?.let {
        val cal = Calendar.getInstance()
        cal.time=it
        val firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH)
        cal[Calendar.DAY_OF_MONTH] = firstDay
        return cal.time
    }
}


/** 指定日期月的最后一天  */
fun Date?.getLastDayOfMonth(): Date? {
    return  this?.let {
        val cal = Calendar.getInstance()
        cal.time=it
        val lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        cal[Calendar.DAY_OF_MONTH] = lastDay
        return cal.time
    }
}
fun Date?.getDayOfWeek(value:Int=Calendar.MONDAY): Date? {
    return this?.let {
        val cal = Calendar.getInstance()
        cal.firstDayOfWeek=Calendar.MONDAY
        cal.time=it
        cal.set(Calendar.DAY_OF_WEEK,value)
        return cal.time
    }
}

/**
 * 获得两个Date型日期之间相差的天数（tarDate减this）
 * @param tarDate java.util.Date
 * @return int 相差的天数
 */
fun Date?.getDaysBetweenDates(tarDate: Date?): Int {
    val d1 = this?.toStr().toDate(DATE_PATTERN)
    val d2 = tarDate?.toStr().toDate(DATE_PATTERN)
    if (d1 == null || d2 == null) return -1
    val mils = (d2.time - d1.time) / TIME_DAY_MILLISECOND
    return mils.toInt()
}
