package com.weiting.tohealth.mystatisticpage.drugchart

import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Drug
import com.weiting.tohealth.getTimeStampToDateInt
import com.weiting.tohealth.mystatisticpage.LogItem
import com.weiting.tohealth.mystatisticpage.ResultInDate
import com.weiting.tohealth.toTimeFromTimeStamp

class AnalyzeDrugLog {

    private val resultInDateList = mutableListOf<ResultInDate>()
    private var resultList = mutableListOf<Map<String, String>>()

    //Get the Date Range. It's the ResultInDate count.
    private val allDateInInt = mutableListOf<Int>()
    private val allDateInTimestamp = mutableListOf<Timestamp>()

    fun revertToResultInDateList(drug: Drug): LogItem.DrugLogItem {
        getAllDate(drug)
        allDateInInt.forEachIndexed { index, it ->
            drug.drugLogs.forEach { drugLog ->
                if (getTimeStampToDateInt(drugLog.createdTime!!) == it) {
                    resultList.add(
                        mapOf(
                            "result" to drugLog.result.toString(),
                            "time" to toTimeFromTimeStamp(drugLog.createdTime)
                        )
                    )
                }
            }
            resultInDateList.add(ResultInDate(date = allDateInTimestamp[index], resultList))
            resultList = mutableListOf()
        }
//        Log.i("data", "${LogItem.DrugLogItem(drug.drugName!!, resultInDateList)}")
        return LogItem.DrugLogItem(drug.drugName!!, resultInDateList)
    }

    private fun getAllDate(drug: Drug) {
        drug.drugLogs.forEach {
            if (getTimeStampToDateInt(it.createdTime!!) !in allDateInInt) {
                allDateInInt.add(getTimeStampToDateInt(it.createdTime))
                allDateInTimestamp.add(it.createdTime)
//                Log.i("data", allDateInInt.toString())
            }
        }
    }
}