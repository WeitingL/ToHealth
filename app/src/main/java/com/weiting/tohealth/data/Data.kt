package com.weiting.tohealth.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import io.grpc.internal.DnsNameResolver
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemDataList(
    val list: List<ItemData> = listOf()
) : Parcelable

//Use this data class to hold all type of item.
@Parcelize
data class ItemData(
    val DrugData: Drug? = null,
    val MeasureData: Measure? = null,
    val ActivityData: Activity? = null,
    val CareData: Care? = null
) : Parcelable

@Parcelize
data class ItemLogData(
    val ItemId: String? = null,
    val ItemType: ItemType? = null,
    val DrugLog: DrugLog? = null,
    val MeasureLog: MeasureLog? = null,
    val ActivityLog : ActivityLog? = null,
    val CareLog: CareLog? = null
):Parcelable

@Parcelize
data class Drug(
    var id: String? = null,
    val userId: String? = null,
    val drugName: String? = null,
    val dose: Int = 0,
    val unit: Int? = null,
    val endDate: Map<String, Int?> = mapOf("type" to null, "day" to null),
    val startDate: Timestamp? = null,
    val period: Map<String, Int?> = mapOf(
        "type" to null,
        "N" to null,
        "X" to null
    ),
    val executeTime: List<Timestamp> = listOf(),
    val stock: Int = 0,
    val editor: String? = null,
    val createTime: Timestamp? = null,
    val status: Int? = null,
    var drugLogs: List<DrugLog> = listOf()
) : Parcelable

@Parcelize
data class DrugLog(
    var id: String? = null,
    val result: Int? = null,
    val createTime: Timestamp? = null
) : Parcelable

@Parcelize
data class Measure(
    var id: String? = null,
    val userId: String? = null,
    val type: Int? = null,
    val executeTime: List<Timestamp> = listOf(),
    val editor: String? = null,
    val createTime: Timestamp? = null,
    val status: Int? = null,
    var measureLogs: List<MeasureLog> = listOf()
) : Parcelable

@Parcelize
data class MeasureLog(
    var id: String? = null,
    val result: Int? = null,
    val record: Map<String, Int?> = mapOf("X" to null, "Y" to null, "Z" to null),
    val createTime: Timestamp? = null
) : Parcelable

@Parcelize
data class Activity(
    var id: String? = null,
    val userId: String? = null,
    val type: Int? = null,
    val endDate: Map<String, Int?> = mapOf("type" to null, "day" to null),
    val period: Map<String, Int?> = mapOf(
        "type" to null,
        "N" to null,
        "X" to null
    ),
    val startDate: Timestamp? = null,
    val executeTime: List<Timestamp> = listOf(),
    val editor: String? = null,
    val createTime: Timestamp? = null,
    val status: Int? = null,
    var activityLogs: List<ActivityLog> = listOf()
) : Parcelable

@Parcelize
data class ActivityLog(
    var id: String? = null,
    val result: Int? = null,
    val record: Map<String, Int?> = mapOf("X" to null, "Y" to null, "Z" to null),
    val createTime: Timestamp? = null
) : Parcelable

@Parcelize
data class Care(
    var id: String? = null,
    val userId: String? = null,
    val type: Int? = null,
    val endDate: Map<String, Int?> = mapOf("type" to null, "day" to null),
    val period: Map<String, Int?> = mapOf(
        "type" to null,
        "N" to null,
        "X" to null
    ),
    val startDate: Timestamp? = null,
    val executeTime: List<Timestamp> = listOf(),
    val editor: String? = null,
    val createTime: Timestamp? = null,
    val status: Int? = null,
    var careLogs: List<CareLog> = listOf()
) : Parcelable

@Parcelize
data class CareLog(
    var id: String? = null,
    val result: Int? = null,
    val record: Map<String, String?> = mapOf("emotion" to null, "note" to null),
    val createTime: Timestamp? = null
) : Parcelable