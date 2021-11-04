package com.weiting.tohealth.mystatisticpage.activitychart

import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Activity
import com.weiting.tohealth.getTimeStampToDateInt
import com.weiting.tohealth.mystatisticpage.LogItem
import com.weiting.tohealth.mystatisticpage.ResultInDate
import com.weiting.tohealth.toActivityType

class AnalyzeActivityLog {

    private val resultInDateList = mutableListOf<ResultInDate>()
    private var resultList = mutableListOf<Int>()

    private val allDateInInt = mutableListOf<Int>()
    private val allDateInTimeStamp = mutableListOf<Timestamp>()

    fun revertToResultInDateList(activity: Activity): LogItem.ActivityLogItem {
        getAllDate(activity)
        allDateInInt.forEachIndexed { index, it ->
            activity.activityLogs.forEach { activityLog ->
                if (getTimeStampToDateInt(activityLog.createTime!!) == it){
                    resultList.add(activityLog.result!!)
                }
            }
            resultInDateList.add(ResultInDate(allDateInTimeStamp[index], resultList))
            resultList = mutableListOf()
        }
        return LogItem.ActivityLogItem(toActivityType(activity.type), resultInDateList)
    }

    private fun getAllDate(activity: Activity) {
        activity.activityLogs.forEach {
            if (getTimeStampToDateInt(it.createTime!!) !in allDateInInt){
                allDateInInt.add(getTimeStampToDateInt(it.createTime))
                allDateInTimeStamp.add(it.createTime)
            }
        }
    }

}