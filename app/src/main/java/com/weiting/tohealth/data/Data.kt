package com.weiting.tohealth.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemDataList(
    val list: List<ItemData> = listOf()
):Parcelable

//Use this data class to hold all type of item.
@Parcelize
data class ItemData(
    val DrugData: Drug? = null,
    val MeasureData: Measure? = null,
    val ActivityData: Activity? = null,
    val CareData: Care? = null
):Parcelable

@Parcelize
data class Drug(
    var id: String? = null,
    val userId : String? = null,
    val drugName: String? = null,
    val dose: Int = 0,
    val unit: Int? = null,
    val endDate: Map<String, Int?> = mapOf("type" to null, "day" to null),
    val startDate: Timestamp? = null,
    val period: Map<String, Int?> = mapOf("type" to null, "N" to null, "X" to null, "Y" to null, "subtype" to null, "Z" to null),
    val firstTimePerDay: Timestamp? = null,
    val stock: Int = 0,
    val editor: String? = null,
    val createTime: Timestamp? = null,
    val status: Int? = null,
    val drugLogs: List<DrugLog> = listOf()
):Parcelable

@Parcelize
data class DrugLog(
    val id: String? = null,
    val result: Int? = null,
    val createTime: Timestamp? = null
):Parcelable

@Parcelize
data class Measure(
    var id: String? = null,
    val userId : String? = null,
    val type: Int? = null,
    val firstTimePerDay: Timestamp? = null,
    val editor: String? = null,
    val createTime: Timestamp? = null,
    val status: Int? = null,
    val measureLogs: List<MeasureLog> = listOf()
):Parcelable

@Parcelize
data class MeasureLog(
    val id: String? = null,
    val result: Int? = null,
    val record:Map<Int, Int?> = mapOf(0 to null, 1 to null, 2 to null),
    val createTime: Timestamp? = null
):Parcelable

@Parcelize
data class Activity(
    var id: String? = null,
    val userId : String? = null,
    val type: Int? = null,
    val endDate: Map<String, Int?> = mapOf("type" to null, "day" to null),
    val period: Map<String, Int?> = mapOf("type" to null, "N" to null, "X" to null, "Y" to null, "subtype" to null, "Z" to null),
    val startDate: Timestamp? = null,
    val firstTimePerDay: Timestamp? = null,
    val editor: String? = null,
    val createTime: Timestamp? = null,
    val status: Int? = null,
    val activityLogs: List<ActivityLog> = listOf()
):Parcelable

@Parcelize
data class ActivityLog(
    val id: String? = null,
    val result: Int? = null,
    val record:Map<Int, Int?> = mapOf(0 to null, 1 to null, 2 to null),
    val createTime: Timestamp? = null
):Parcelable

@Parcelize
data class Care(
    var id: String? = null,
    val userId : String? = null,
    val type: Int? = null,
    val endDate: Map<String, Int?> = mapOf("type" to null, "day" to null),
    val period: Map<String, Int?> = mapOf("type" to null, "N" to null, "X" to null, "Y" to null, "subtype" to null, "Z" to null),
    val startDate: Timestamp? = null,
    val firstTimePerDay: Timestamp? = null,
    val editor: String? = null,
    val createTime: Timestamp? = null,
    val status: Int? = null,
    val careLogs: List<CareLog> = listOf()
):Parcelable

@Parcelize
data class CareLog(
    val id: String? = null,
    val result: Int? = null,
    val record : Map<String, String?> = mapOf("emotion" to null, "note" to null),
    val createTime: Timestamp? = null
):Parcelable