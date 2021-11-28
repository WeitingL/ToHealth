package com.weiting.tohealth.homepage

import com.weiting.tohealth.data.ItemType

class TodoListProgressCounter {

    var drugPostNum = 0
    var measurePostNum = 0
    var eventPostNum = 0
    var carePostNum = 0

    fun postLog(swipeData: SwipeData) {
        when (swipeData.itemDataType) {
            is ItemDataType.DrugType -> postOneDrugLog()
            is ItemDataType.MeasureType -> postOneMeasureLog()
            is ItemDataType.CareType -> postOneCareLog()
            is ItemDataType.EventType -> postOneEventLog()
            else -> {
            }
        }
    }

    fun getCounterRecord(itemType: ItemType):Int {
        return when(itemType){
            ItemType.DRUG -> drugPostNum
            ItemType.EVENT -> eventPostNum
            ItemType.MEASURE -> measurePostNum
            ItemType.CARE -> carePostNum
        }
    }

    private fun postOneDrugLog() {
        drugPostNum++
    }

    private fun postOneMeasureLog() {
        measurePostNum++
    }

    private fun postOneEventLog() {
        eventPostNum++
    }

    private fun postOneCareLog() {
        carePostNum++
    }

}