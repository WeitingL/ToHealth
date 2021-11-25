package com.weiting.tohealth.mystatisticpage.measurechart

import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Measure
import com.weiting.tohealth.mystatisticpage.LogItem
import com.weiting.tohealth.mystatisticpage.ResultInDateForMeasure
import com.weiting.tohealth.util.Util.toMeasureType

class AnalyzeMeasureLog {

    private val resultInDateList = mutableListOf<ResultInDateForMeasure>()

    fun revertToResultInDateList(measure: Measure): LogItem.MeasureLogItem {

        measure.measureLogs.forEach {
            resultInDateList.add(
                ResultInDateForMeasure(
                    it.createdTime ?: Timestamp.now(),
                    it.result ?:0,
                    it.record
                )
            )
        }

        return LogItem.MeasureLogItem(toMeasureType(measure.type), measure.type?:0, resultInDateList)
    }
}
