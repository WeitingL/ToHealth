package com.weiting.tohealth.mystatisticpage.drugchart

import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Drug
import com.weiting.tohealth.mystatisticpage.LogItem
import com.weiting.tohealth.mystatisticpage.ResultInDate
import com.weiting.tohealth.util.Util.getTimeStampToDateInt
import com.weiting.tohealth.util.Util.getTimeStampToTimeString

class AnalyzeDrugLog {

    private val resultInDateList = mutableListOf<ResultInDate>()
    private var resultList = mutableListOf<Map<String, String>>()

    // Get the Date Range. It's the ResultInDate count.
    private val allDateInInt = mutableListOf<Int>()
    private val allDateInTimestamp = mutableListOf<Timestamp>()

    fun revertToResultInDateList(drug: Drug): LogItem.DrugLogItem {
        getAllDate(drug)
        allDateInInt.forEachIndexed { index, it ->
            drug.drugLogs.forEach { drugLog ->
                if (getTimeStampToDateInt(drugLog.createdTime ?: Timestamp.now()) == it) {
                    resultList.add(
                        mapOf(
                            "result" to drugLog.result.toString(),
                            "time" to getTimeStampToTimeString(drugLog.createdTime)
                        )
                    )
                }
            }
            resultInDateList.add(ResultInDate(date = allDateInTimestamp[index], resultList))
            resultList = mutableListOf()
        }
        return LogItem.DrugLogItem(drug.drugName ?: "", resultInDateList)
    }

    private fun getAllDate(drug: Drug) {
        drug.drugLogs.forEach {
            if (getTimeStampToDateInt(it.createdTime ?: Timestamp.now()) !in allDateInInt) {
                allDateInInt.add(getTimeStampToDateInt(it.createdTime ?: Timestamp.now()))
                allDateInTimestamp.add(it.createdTime ?: Timestamp.now())
            }
        }
    }
}
