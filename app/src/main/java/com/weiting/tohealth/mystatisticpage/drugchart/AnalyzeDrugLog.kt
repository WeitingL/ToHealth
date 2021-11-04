package com.weiting.tohealth.mystatisticpage.drugchart

import android.util.Log
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Drug
import com.weiting.tohealth.getTimeStampToDateInt
import com.weiting.tohealth.mystatisticpage.LogItem
import com.weiting.tohealth.mystatisticpage.ResultInDate

class AnalyzeDrugLog {

    private val resultInDateList = mutableListOf<ResultInDate>()
    private var resultList = mutableListOf<Int>()

    //TODO Rearrange unexpected uptake to the last.

    //Get the Date Range. It's the ResultInDate count.
    private val allDateInInt = mutableListOf<Int>()
    private val allDateInTimestamp = mutableListOf<Timestamp>()

    fun revertToResultInDateList(drug: Drug): LogItem.DrugLogItem {
            getAllDate(drug)
            allDateInInt.forEachIndexed { index, it ->
                drug.drugLogs.forEach { drugLog ->
                    if (getTimeStampToDateInt(drugLog.createTime!!) == it) {
                        resultList.add(drugLog.result!!)
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
            if (getTimeStampToDateInt(it.createTime!!) !in allDateInInt){
                allDateInInt.add(getTimeStampToDateInt(it.createTime))
                allDateInTimestamp.add(it.createTime)
//                Log.i("data", allDateInInt.toString())
            }
        }
    }
}