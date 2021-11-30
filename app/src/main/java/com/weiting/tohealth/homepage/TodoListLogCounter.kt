package com.weiting.tohealth.homepage


import android.util.Log
import com.weiting.tohealth.data.ItemType

class TodoListLogCounter {

    /*
        Provide the counts to help refresh counting finished mission.
     */

    //Already post checked counts.
    private var drugPostNum = 0
    private var measurePostNum = 0
    private var eventPostNum = 0
    private var carePostNum = 0

    private var lastItemType = ItemType.DRUG

    fun addPostLogCount(itemType: ItemType) {
        when (itemType) {
            ItemType.DRUG -> drugPostNum++
            ItemType.EVENT -> measurePostNum++
            ItemType.MEASURE -> eventPostNum++
            ItemType.CARE -> carePostNum++
        }
    }

    fun addPostLogCount(swipeData: SwipeData) {
        when (swipeData.itemDataType) {
            is ItemDataType.TimeType -> {
            }
            is ItemDataType.CareType -> {
                carePostNum++
                lastItemType = ItemType.CARE
            }
            is ItemDataType.DrugType -> {
            }
            is ItemDataType.EventType -> {
                eventPostNum++
                lastItemType = ItemType.EVENT
            }
            is ItemDataType.MeasureType -> {
                measurePostNum++
                lastItemType = ItemType.MEASURE
            }
        }
    }

    fun decreasePostLogCount() {
        Log.i("type", lastItemType.toString())
        when (lastItemType) {
            ItemType.CARE -> carePostNum--
            ItemType.DRUG -> {
            }
            ItemType.EVENT -> eventPostNum--
            ItemType.MEASURE -> measurePostNum--
        }
    }

    fun getTotalCheckedLogNum(): Int {
        return drugPostNum + measurePostNum + eventPostNum + carePostNum
    }

    fun clearCount(itemType: ItemType) {
        when (itemType) {
            ItemType.DRUG -> drugPostNum = 0
            ItemType.EVENT -> eventPostNum = 0
            ItemType.MEASURE -> measurePostNum = 0
            ItemType.CARE -> carePostNum = 0
        }
    }
}