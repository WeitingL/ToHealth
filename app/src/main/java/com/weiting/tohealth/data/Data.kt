package com.weiting.tohealth.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

//Use this data class to hold all type of item.
@Parcelize
data class ItemData(
    val DrugData: Drug? = null,
    val MeasureData: Measure? = null,
    val ActivityData: Activity? = null,
    val CareData: Care? = null
) : Parcelable

@Parcelize
data class Drug(
    var id: String? = null,
    val userId: String? = null,
    val drugName: String? = null,
    var dose: Float = 0F,
    var unit: Int? = null,
    val endDate: Map<String, Int?> = mapOf("type" to null, "day" to null),
    val startDate: Timestamp? = null,
    var period: Map<String, Int?> = mapOf(
        "type" to null,
        "N" to null,
        "X" to null
    ),
    var executedTime: List<Timestamp> = listOf(),
    var stock: Float = 0F,
    var editor: String? = null,
    val createdTime: Timestamp? = null,
    var lastEditTime: Timestamp? = null,
    var status: Int? = null,
    var drugLogs: List<DrugLog> = listOf()
) : Parcelable, ItemsDataType

@Parcelize
data class DrugLog(
    var id: String? = null,
    val timeTag: Int? = null,
    val result: Int? = null,
    val createdTime: Timestamp? = null
) : Parcelable, ItemsLogType

@Parcelize
data class Measure(
    var id: String? = null,
    val userId: String? = null,
    val type: Int? = null,
    var executedTime: List<Timestamp> = listOf(),
    var editor: String? = null,
    val createdTime: Timestamp? = null,
    var lastEditTime: Timestamp? = null,
    var status: Int? = null,
    var measureLogs: List<MeasureLog> = listOf()
) : Parcelable, ItemsDataType

@Parcelize
data class MeasureLog(
    var id: String? = null,
    val timeTag: Int? = null,
    val result: Int? = null,
    val record: Map<String, Int?> = mapOf("X" to null, "Y" to null, "Z" to null),
    val createdTime: Timestamp? = null
) : Parcelable, ItemsLogType

@Parcelize
data class Activity(
    var id: String? = null,
    val userId: String? = null,
    val type: Int? = null,
    val endDate: Map<String, Int?> = mapOf("type" to null, "day" to null),
    var period: Map<String, Int?> = mapOf(
        "type" to null,
        "N" to null,
        "X" to null
    ),
    val startDate: Timestamp? = null,
    var executedTime: List<Timestamp> = listOf(),
    var editor: String? = null,
    val createdTime: Timestamp? = null,
    var lastEditTime: Timestamp? = null,
    var status: Int? = null,
    var activityLogs: List<ActivityLog> = listOf()
) : Parcelable, ItemsDataType

@Parcelize
data class ActivityLog(
    var id: String? = null,
    val timeTag: Int? = null,
    val result: Int? = null,
    val record: Map<String, Int?> = mapOf("X" to null, "Y" to null, "Z" to null),
    val createdTime: Timestamp? = null
) : Parcelable, ItemsLogType

@Parcelize
data class Care(
    var id: String? = null,
    val userId: String? = null,
    val type: Int? = null,
    val endDate: Map<String, Int?> = mapOf("type" to null, "day" to null),
    var period: Map<String, Int?> = mapOf(
        "type" to null,
        "N" to null,
        "X" to null
    ),
    val startDate: Timestamp? = null,
    var executeTime: List<Timestamp> = listOf(),
    var editor: String? = null,
    val createdTime: Timestamp? = null,
    var lastEditTime: Timestamp? = null,
    var status: Int? = null,
    var careLogs: List<CareLog> = listOf()
) : Parcelable, ItemsDataType

@Parcelize
data class CareLog(
    var id: String? = null,
    val timeTag: Int? = null,
    val result: Int? = null,
    var record: Map<String, String?> = mapOf("emotion" to null, "note" to null),
    val createdTime: Timestamp? = null
) : Parcelable, ItemsLogType

interface ItemsDataType

interface ItemsLogType