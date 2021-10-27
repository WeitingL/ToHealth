package com.weiting.tohealth

import android.util.Log
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun toUnit(int: Int?): String {
    return when (int) {
        0 -> "毫克"
        1 -> "毫升"
        2 -> "針劑"
        3 -> "單位"
        4 -> "滴"
        5 -> "片"
        6 -> "顆"
        7 -> "湯匙"
        8 -> "袋/包"
        9 -> "個"
        10 -> "次"
        else -> "其他單位"
    }
}

fun toEndDate(map: Map<String, Int?>?): String {
    return when (map?.get("type")) {
        0 -> "無期限"
        1 -> "還有 ${map["day"]} 天"
        2 -> "還有 ${map["day"]} 天"
        else -> "無期限"
    }
}

fun toStatus(int: Int?): String {
    return when (int) {
        0 -> "執行中"
        1 -> "不執行"
        2 -> "暫停執行"
        else -> "未知狀態"
    }
}

fun toFooter(int: Int?): String {
    return when (int) {
        0 -> "留"
        1 -> "關心您"
        2 -> "紀錄"
        3 -> "通知"
        4 -> "祝福"
        5 -> "改"
        else -> "What?"
    }
}

fun toMeasureType(int: Int?): String {
    return when (int) {
        0 -> "血壓"
        1 -> "飯前血糖"
        2 -> "飯後血糖"
        3 -> "血氧"
        4 -> "體重"
        5 -> "體溫"
        else -> "What?"
    }
}

fun toActivityType(int: Int?): String {
    return when (int) {
        0 -> "健身"
        1 -> "走路(健走"
        2 -> "物理治療"
        3 -> "跑步"
        4 -> "呼吸訓練"
        5 -> "心理諮詢"
        6 -> "冥想"
        7 -> "瑜珈(伸展)"
        8 -> "眼部運動"
        9 -> "中醫治療"
        10 -> "洗腎"
        else -> "What?"
    }
}

fun toCareType(int: Int?): String {
    return when (int) {
        0 -> "心情關懷"
        1 -> "疼痛關懷"
        2 -> "症狀關懷"
        3 -> "睡眠關懷"
        else -> "What?"
    }
}

fun setDrugDrawable(int: Int?): Int {
    return when (int) {
        0 -> R.drawable.medicine
        1 -> R.drawable.medicine
        2 -> R.drawable.syringe
        3 -> R.drawable.medicine
        4 -> R.drawable.pipette
        5 -> R.drawable.medicine
        6 -> R.drawable.pills
        7 -> R.drawable.medicine
        8 -> R.drawable.medicine
        9 -> R.drawable.pills
        10 -> R.drawable.medicine
        else -> R.drawable.medicine
    }
}

fun setMeasureDrawable(int: Int?): Int {
    return when (int) {
        0 -> R.drawable.blood_pressure
        1 -> R.drawable.sugar_blood_level
        2 -> R.drawable.loupe
        3 -> R.drawable.body_scale
        4 -> R.drawable.thermometer
        else -> R.drawable.loupe
    }
}

fun setActivityType(int: Int?): Int {
    return when (int) {
        0 -> R.drawable.strength
        1 -> R.drawable.running
        2 -> R.drawable.doctor
        3 -> R.drawable.running
        4 -> R.drawable.exercise
        5 -> R.drawable.hospital_sign
        6 -> R.drawable.exercise
        7 -> R.drawable.exercise
        8 -> R.drawable.eye
        9 -> R.drawable.doctor
        10 -> R.drawable.hospital_sign
        else -> R.drawable.hospital_sign
    }
}

fun toStringFromTimeStamp(timestamp: Timestamp?): String {
    return SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.TAIWAN).format(timestamp!!.toDate())
        .toString()
}

fun toTimeFromTimeStamp(timestamp: Timestamp?): String {
    Log.i("Time", timestamp.toString())
    return SimpleDateFormat("HH:mm", Locale.TAIWAN).format(timestamp!!.toDate()).toString()
}

fun toTimeInMilliFromPicker(y: Int, M: Int, d: Int, h: Int, m: Int): Long {
    val time = Calendar.getInstance()
    time.set(Calendar.YEAR, y)
    time.set(Calendar.MONTH, M)
    time.set(Calendar.DAY_OF_MONTH, d)
    time.set(Calendar.HOUR_OF_DAY, h)
    time.set(Calendar.MINUTE, m)
//    Log.i("Time? FromDateAndTime", "${Timestamp(Date(time.timeInMillis)).toDate()}")
    return time.timeInMillis
}

fun toDateAndTimeFromMilliTime(millis: Long): String {
    val Format = SimpleDateFormat("yyyy/MM/dd hh:mm", Locale.TAIWAN)
//    Log.i("date", "${Formate.format(Date(millis))}")
    return Format.format(Date(millis))
}

fun toDateFromMilliTime(millis: Long): String {
    val Format = SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN)
//    Log.i("date", "${Formate.format(Date(millis))}")
    return Format.format(Date(millis))
}

fun toTimeFromMilliTime(millis: Long): String {
    val Format = SimpleDateFormat("hh:mm", Locale.TAIWAN)
//    Log.i("date", "${Formate.format(Date(millis))}")
    return Format.format(Date(millis))
}

fun toStringFromPeriod(data: Map<String, Int?>): String {

    return when (data["type"]) {
        0 -> {
            "每日執行"
        }
        1 -> {
            "每${data["N"]?.plus(1)}日執行"
        }
        2 -> {
            "每個 禮拜${toWeek(data["N"] ?: 0)}執行"
        }

        3 -> {
            "連續執行${toCycleValue(data["N"] ?: 0)}後，再暫停${toCycleValue(data["X"] ?: 0)}"
        }

        4 -> {
            "依需求執行"
        }

        else -> "Others."
    }
}

fun toCycleValue(int: Int): String {
    return when (int) {
        0 -> "1日"
        1 -> "2日"
        2 -> "3日"
        3 -> "4日"
        4 -> "5日"
        5 -> "6日"
        6 -> "7日"
        7 -> "8日"
        8 -> "9日"
        9 -> "10日"
        10 -> "11日"
        11 -> "12日"
        12 -> "13日"
        13 -> "2週"
        14 -> "3週"
        15 -> "1個月"
        else -> "What?!"
    }
}

fun toWeek(int: Int): String {
    return when (int) {
        0 -> "星期一"
        1 -> "星期二"
        2 -> "星期三"
        3 -> "星期四"
        4 -> "星期五"
        5 -> "星期六"
        6 -> "星期日"
        else -> "What?!"
    }
}