package com.weiting.tohealth.homepage

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
        drugSwipeData.clear()
        return data
    }

    fun giveDrugLogIdForUndo(): String {
        val data = drugLogIds.last()
        drugLogIds.clear()
        return data
    }

    fun giveDrugSwipeDataForPost(): SwipeData {
        return drugSwipeData.last()
    }

    fun giveEventSwipeData():SwipeData{
        val data = eventSwipeData.last()
        eventSwipeData.clear()
        eventTimeList.clear()
        return data
    }

    fun reBuildCurrentList(currentList: MutableList<ItemDataType>): MutableList<ItemDataType> {

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