package com.weiting.tohealth.mystatisticpage.carechart

import android.util.Log
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Care
import com.weiting.tohealth.getTimeStampToDateInt
import com.weiting.tohealth.mystatisticpage.LogItem
import com.weiting.tohealth.mystatisticpage.ResultInDate
import com.weiting.tohealth.mystatisticpage.ResultInDateForCare
import com.weiting.tohealth.toActivityType
import com.weiting.tohealth.toCareType

class AnalyzeCareLog {

    private val resultInDateList = mutableListOf<ResultInDateForCare>()

    fun revertToResultInDateList(care: Care): LogItem.CareLogItem {

        care.careLogs.forEach {
            resultInDateList.add(ResultInDateForCare(it.createTime!!, 8 - it.record["emotion"]!!.toInt(), it.record["note"]!!))
        }
        return LogItem.CareLogItem(toCareType(care.type), resultInDateList)
    }
}