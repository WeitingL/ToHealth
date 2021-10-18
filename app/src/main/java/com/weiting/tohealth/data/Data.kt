package com.weiting.tohealth.data

import com.google.firebase.Timestamp

data class Drug(
    var id: String? = null,
    val userId : String? = null,
    val drugName: String? = null,
    val dose: Int = 0,
    val unit: Int? = null,
    val endDate: Map<String, Int?> = mapOf("type" to null, "day" to null),
    val period: Map<String, Int?> = mapOf("type" to null, "N" to null, "X" to null, "Y" to null),
    val firstTimePerDay: Int? = null,
    val stock: Int = 0,
    val editor: String? = null,
    val createTime: Timestamp? = null,
    val status: Int? = null,
    val drugLogs: List<DrugLog> = listOf()
)

data class DrugLog(
    val id: String? = null,
    val result: Int? = null,
    val createTime: Timestamp? = null
)

data class Measure(
    val id: String? = null,
    val userId : String? = null,
    val type: Int? = null,
    val firstTimePerDay: Int,
    val editor: String? = null,
    val createTime: Timestamp? = null,
    val status: Int? = null,
    val measureLogs: List<MeasureLog> = listOf()
)

data class MeasureLog(
    val id: String? = null,
    val result: Int? = null,
    val record:Map<Int, Int?> = mapOf(0 to null, 1 to null, 2 to null),
    val createTime: Timestamp? = null
)

data class Activity(
    val id: String? = null,
    val userId : String? = null,
    val type: Int? = null,
    val endDate: Map<String, Int?> = mapOf("type" to null, "day" to null),
    val period: Map<String, Int?> = mapOf("type" to null, "N" to null, "X" to null, "Y" to null),
    val firstTimePerDay: Int,
    val editor: String? = null,
    val createTime: Timestamp? = null,
    val status: Int? = null,
    val activityLogs: List<ActivityLog> = listOf()
)

data class ActivityLog(
    val id: String? = null,
    val result: Int? = null,
    val record:Map<Int, Int?> = mapOf(0 to null, 1 to null, 2 to null),
    val createTime: Timestamp? = null
)

data class Care(
    val id: String? = null,
    val userId : String? = null,
    val type: Int? = null,
    val endDate: Map<String, Int?> = mapOf("type" to null, "day" to null),
    val period: Map<String, Int?> = mapOf("type" to null, "N" to null, "X" to null, "Y" to null),
    val firstTimePerDay: Int,
    val editor: String? = null,
    val createTime: Timestamp? = null,
    val status: Int? = null,
    val careLogs: List<CareLog> = listOf()
)

data class CareLog(
    val id: String? = null,
    val result: Int? = null,
    val record : Map<String, String?> = mapOf("emotion" to null, "note" to null),
    val createTime: Timestamp? = null
)