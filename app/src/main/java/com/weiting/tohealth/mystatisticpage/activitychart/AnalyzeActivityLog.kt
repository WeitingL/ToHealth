package com.weiting.tohealth.mystatisticpage.activitychart

import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Activity
import com.weiting.tohealth.mystatisticpage.LogItem
import com.weiting.tohealth.mystatisticpage.ResultInDate
import com.weiting.tohealth.util.Util.getTimeStampToDateInt
import com.weiting.tohealth.util.Util.toActivityType
import com.weiting.tohealth.util.Util.toTimeFromTimeStamp

class AnalyzeActivityLog {

    private val resultInDateList = mutableListOf<ResultInDate>()
    private var resultList = mutableListOf<Map<String, String>>()

    private val allDateInInt = mutableListOf<Int>()
    private val allDateInTimeStamp = mutableListOf<Timestamp>()

    fun revertToResultInDateList(activity: Activity): LogItem.ActivityLogItem {
        getAllDate(activity)
        allDateInInt.forEachIndexed { index, it ->
            activity.activityLogs.forEach { activityLog ->
                if (getTimeStampToDateInt(activityLog.createdTime?: Timestamp.now()) == it) {
                    resultList.add(
                        mapOf(
                            "result" to activityLog.result.toString(),
                            "time" to toTimeFromTimeStamp(activityLog.createdTime)
                        )
                    )
                }
            }
            resultInDateList.add(ResultInDate(allDateInTimeStamp[index], resultList))
            resultList = mutableListOf()
        }
        return LogItem.ActivityLogItem(toActivityType(activity.type), resultInDateList)
    }

    private fun getAllDate(activity: Activity) {
        activity.activityLogs.forEach {
            if (getTimeStampToDateInt(it.createdTime ?: Timestamp.now()) !in allDateInInt) {
                allDateInInt.add(getTimeStampToDateInt(it.createdTime ?: Timestamp.now()))
                allDateInTimeStamp.add(it.createdTime ?: Timestamp.now())
            }
        }
    }

}