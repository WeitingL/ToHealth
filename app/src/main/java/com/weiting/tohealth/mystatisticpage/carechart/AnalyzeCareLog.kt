package com.weiting.tohealth.mystatisticpage.carechart

import com.weiting.tohealth.data.Care
import com.weiting.tohealth.mystatisticpage.LogItem
import com.weiting.tohealth.mystatisticpage.ResultInDateForCare
import com.weiting.tohealth.toCareType

class AnalyzeCareLog {

    private val resultInDateList = mutableListOf<ResultInDateForCare>()

    fun revertToResultInDateList(care: Care): LogItem.CareLogItem {

        care.careLogs.forEach {
            if (it.record["emotion"] != null){
                resultInDateList.add(ResultInDateForCare(it.createdTime!!, 8 - it.record["emotion"]!!.toInt(), it.record["note"]?:""))
            }else{
                resultInDateList.add(ResultInDateForCare(it.createdTime!!, null, it.record["note"]?:""))
            }

        }
        return LogItem.CareLogItem(toCareType(care.type), resultInDateList)
    }
}