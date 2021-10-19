package com.weiting.tohealth

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