package com.weiting.tohealth.homepage

import android.util.Log

class SwipeCheckedListManager {

    private val drugSwipeData = mutableListOf<SwipeData>()
    private val drugLogIds= mutableListOf<String>()

    private val eventSwipeData = mutableListOf<SwipeData>()
    private val eventTimeList = mutableListOf<SwipeData>()

    fun getDrugSwipeData (swipeData: SwipeData){
        drugSwipeData.add(swipeData)
    }

    fun getEventSwipeData(swipeData: SwipeData){
        eventSwipeData.add(swipeData)
    }

    fun getEventTimeHeader(swipeData: SwipeData){
        eventTimeList.add(swipeData)
    }

    fun getDrugLogId(drugLogId: String){
        drugLogIds.add(drugLogId)
    }

    fun giveDrugSwipeDataForUndo(): SwipeData {
        val data = drugSwipeData.last()
        drugSwipeData.removeLast()
        return data
    }

    fun giveDrugLogIdForUndo(): String {
        val data = drugLogIds.last()
        drugLogIds.removeLast()
        return data
    }

    fun giveDrugSwipeDataForPost(): SwipeData {
        return drugSwipeData.last()
    }

    fun giveEventSwipeData():SwipeData{
        val data = eventSwipeData.first()
        eventSwipeData.removeFirst()
        eventTimeList.clear()
        return data
    }

    fun reBuildCurrentList(currentList: MutableList<ItemDataType>): MutableList<ItemDataType> {
        Log.i("reBuildCurrentList", eventSwipeData.toString())

        if (eventSwipeData.isNotEmpty()) {
            val lastSwipeData = eventSwipeData.last()


            if (eventTimeList.isNotEmpty() && lastSwipeData.position == (eventTimeList.last().position +1)) {
                currentList.add(eventTimeList.last().position, eventTimeList.last().itemDataType)
                eventTimeList.removeLast()
            }

            currentList.add(lastSwipeData.position, lastSwipeData.itemDataType)
            eventSwipeData.removeLast()
        }
        return currentList
    }


}