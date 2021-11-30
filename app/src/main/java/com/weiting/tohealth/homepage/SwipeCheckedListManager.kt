package com.weiting.tohealth.homepage


class SwipeCheckedListManager {

    private val drugSwipeData = mutableListOf<SwipeData>()
    private val drugLogIds= mutableListOf<String>()

    private val eventSwipeData = mutableListOf<SwipeData>()
    private val eventTimeList = mutableListOf<SwipeData>()

    fun addDrugSwipeData (swipeData: SwipeData){
        drugSwipeData.add(swipeData)
    }

    fun addEventSwipeData(swipeData: SwipeData){
        eventSwipeData.add(swipeData)
    }

    fun addEventTimeHeader(swipeData: SwipeData){
        eventTimeList.add(swipeData)
    }

    fun addDrugLogId(drugLogId: String){
        drugLogIds.add(drugLogId)
    }

    fun getDrugSwipeDataForUndo(): SwipeData {
        val data = drugSwipeData.last()
        drugSwipeData.removeLast()
        return data
    }

    fun getDrugLogIdForUndo(): String {
        val data = drugLogIds.last()
        drugLogIds.removeLast()
        return data
    }

    fun getDrugSwipeDataForPost(): SwipeData {
        return drugSwipeData.last()
    }

    fun getEventSwipeData():SwipeData{
        val data = eventSwipeData.first()
        eventSwipeData.removeFirst()
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