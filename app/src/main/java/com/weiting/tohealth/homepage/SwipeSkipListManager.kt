package com.weiting.tohealth.homepage

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SwipeSkipListManager {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    val skipList = mutableListOf<SwipeData>()
    val skipTimeList = mutableListOf<SwipeData>()

    fun getSwipeToSkip(swipeData: SwipeData) {
        when (swipeData.itemDataType) {
            is ItemDataType.TimeType -> {
                skipTimeList.add(swipeData)
            }
            else -> {
                skipList.add(swipeData)
            }
        }
    }

    fun reBuildCurrentList(currentList: MutableList<ItemDataType>): MutableList<ItemDataType> {

        if (skipList.isNotEmpty()) {
            val lastSwipeData = skipList.last()

            if (skipTimeList.isNotEmpty() && lastSwipeData.position == (skipTimeList.last().position +1)) {
                currentList.add(skipTimeList.last().position, skipTimeList.last().itemDataType)
                skipTimeList.removeLast()
            }

            currentList.add(lastSwipeData.position, lastSwipeData.itemDataType)
            skipList.removeLast()
        }
        return currentList
    }




}