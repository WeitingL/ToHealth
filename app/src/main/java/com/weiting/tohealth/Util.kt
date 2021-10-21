package com.weiting.tohealth

import android.util.Log
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun toUnit(int: Int?): String{
    return when(int){
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

fun toEndDate(map: Map<String, Int?>?):String{
    return when (map?.get("type")){
        0 -> "無期限"
        1 -> "還有 ${map["day"]} 天"
        2 -> "還有 ${map["day"]} 天"
        else -> "無期限"
    }
}

fun toStatus(int: Int?):String {
    return when (int) {
        0 -> "執行中"
        1 -> "不執行"
        2 -> "暫停執行"
        else -> "未知狀態"
    }
}

fun toFooter(int: Int?): String{
    return when(int){
        0 -> "留"
        1 -> "關心您"
        2 -> "紀錄"
        3 -> "通知"
        4 -> "祝福"
        5 -> "改"
        else -> "What?"
    }
}

fun toStringFromTimeStamp(timestamp: Timestamp?):String{
    return SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.TAIWAN).format(timestamp!!.toDate()).toString()
}

fun toTimeFromTimeStamp(timestamp: Timestamp?):String{
    return SimpleDateFormat("HH:mm", Locale.TAIWAN).format(timestamp!!.toDate()).toString()
}

fun toTimeInMilliFromPicker(y:Int, M: Int, d: Int, h:Int, m:Int):Long{
    val time = Calendar.getInstance()
    time.set(Calendar.YEAR, y)
    time.set(Calendar.MONTH, M)
    time.set(Calendar.DAY_OF_MONTH, d)
    time.set(Calendar.HOUR_OF_DAY, h)
    time.set(Calendar.MINUTE, m)
    Log.i("Time? FromDateAndTime", "${Timestamp(Date(time.timeInMillis)).toDate()}")
    return time.timeInMillis
}

fun toDateAndTimeFromMilliTime(millis: Long) : String{
    val Format = SimpleDateFormat("yyyy/MM/dd hh:mm", Locale.TAIWAN)
//    Log.i("date", "${Formate.format(Date(millis))}")
    return Format.format(Date(millis))
}

fun toDateFromMilliTime(millis: Long) : String{
    val Format = SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN)
//    Log.i("date", "${Formate.format(Date(millis))}")
    return Format.format(Date(millis))
}

fun toTimeFromMilliTime(millis: Long) : String{
    val Format = SimpleDateFormat("hh:mm", Locale.TAIWAN)
//    Log.i("date", "${Formate.format(Date(millis))}")
    return Format.format(Date(millis))
}