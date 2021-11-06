package com.weiting.tohealth.mystatisticpage.measurechart

import android.util.Log
import com.weiting.tohealth.data.Measure
import com.weiting.tohealth.mystatisticpage.LogItem
import com.weiting.tohealth.mystatisticpage.ResultInDateForCare
import com.weiting.tohealth.mystatisticpage.ResultInDateForMeasure
import com.weiting.tohealth.toMeasureType

class AnalyzeMeasureLog {

    private val resultInDateList = mutableListOf<ResultInDateForMeasure>()

    fun revertToResultInDateList(measure: Measure): LogItem.MeasureLogItem{

        measure.measureLogs.forEach {
            resultInDateList.add(ResultInDateForMeasure(it.createTime!!, it.result!!, it.record))
        }

        return LogItem.MeasureLogItem(toMeasureType(measure.type), measure.type!!, resultInDateList)
    }
}