package com.weiting.tohealth.mystatisticpage.activitychart

import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Event
import com.weiting.tohealth.mystatisticpage.LogItem
import com.weiting.tohealth.mystatisticpage.ResultInDate
import com.weiting.tohealth.util.Util.getTimeStampToDateInt
import com.weiting.tohealth.util.Util.toEventType
import com.weiting.tohealth.util.Util.toTimeFromTimeStamp

class AnalyzeActivityLog {

    private val resultInDateList = mutableListOf<ResultInDate>()
    private var resultList = mutableListOf<Map<String, String>>()

    private val allDateInInt = mutableListOf<Int>()
    private val allDateInTimeStamp = mutableListOf<Timestamp>()

    fun revertToResultInDateList(event: Event): LogItem.EventLogItem {
        getAllDate(event)
        allDateInInt.forEachIndexed { index, it ->
            event.eventLogs.forEach { activityLog ->
                if (getTimeStampToDateInt(activityLog.createdTime ?: Timestamp.now()) == it) {
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
        return LogItem.EventLogItem(toEventType(event.type), resultInDateList)
    }

    private fun getAllDate(event: Event) {
        event.eventLogs.forEach {
            if (getTimeStampToDateInt(it.createdTime ?: Timestamp.now()) !in allDateInInt) {
                allDateInInt.add(getTimeStampToDateInt(it.createdTime ?: Timestamp.now()))
                allDateInTimeStamp.add(it.createdTime ?: Timestamp.now())
            }
        }
    }
}
