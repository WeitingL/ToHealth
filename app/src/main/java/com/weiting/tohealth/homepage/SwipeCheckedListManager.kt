package com.weiting.tohealth.homepage

class SwipeCheckedListManager {

    val finishedLogList = mutableListOf<SwipeData>()
    val finishedTimeList = mutableListOf<SwipeData>()

    fun getSwipeToChecked(swipeData: SwipeData) {
        when (swipeData.itemDataType) {
            is ItemDataType.TimeType -> {
                finishedTimeList.add(swipeData)
            }
            else -> {
                finishedLogList.add(swipeData)
            }
        }
    }

    fun hasCheckedLogNotPost(): Boolean {
        return when (finishedLogList.isNotEmpty()) {
            true -> true
            false -> false
        }
    }

    fun reBuildCurrentList(currentList: MutableList<ItemDataType>): MutableList<ItemDataType> {

        if (finishedLogList.isNotEmpty()) {
            val lastSwipeData = finishedLogList.last()

            if (finishedTimeList.isNotEmpty() && lastSwipeData.position == (finishedTimeList.last().position + 1)) {
                currentList.add(
                    finishedTimeList.last().position,
                    finishedTimeList.last().itemDataType
                )
                finishedTimeList.removeLast()
            }

            currentList.add(lastSwipeData.position, lastSwipeData.itemDataType)
            finishedLogList.removeLast()
        }
        return currentList
    }

}