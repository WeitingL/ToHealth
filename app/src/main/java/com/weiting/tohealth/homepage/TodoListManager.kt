package com.weiting.tohealth.homepage

import com.weiting.tohealth.data.*

class TodoListManager {

    /*
        Store the all todolist mission below.
     */
    private val drugItemList = mutableListOf<ItemData>()
    private val measureItemList = mutableListOf<ItemData>()
    private val eventItemList = mutableListOf<ItemData>()
    private val careItemList = mutableListOf<ItemData>()

    /*
        Store the unchecked todolist mission item below.
     */
    private val drugCurrentList = mutableListOf<ItemDataType>()
    private val measureCurrentList = mutableListOf<ItemDataType>()
    private val eventCurrentList = mutableListOf<ItemDataType>()
    private val careCurrentList = mutableListOf<ItemDataType>()

    private val timeCurrentList = mutableListOf<ItemDataType>()
    val timeTitleList = mutableListOf<Int>()

    fun addCurrentItemDataType(itemDataType: ItemDataType) {
        when (itemDataType) {
            is ItemDataType.TimeType -> timeCurrentList.add(itemDataType)
            is ItemDataType.CareType -> careCurrentList.add(itemDataType)
            is ItemDataType.DrugType -> drugCurrentList.add(itemDataType)
            is ItemDataType.EventType -> eventCurrentList.add(itemDataType)
            is ItemDataType.MeasureType -> measureCurrentList.add(itemDataType)
        }
    }

    fun clearAllMissionList(itemType: ItemType){
        when(itemType){
            ItemType.DRUG -> drugItemList.clear()
            ItemType.EVENT -> eventItemList.clear()
            ItemType.MEASURE -> measureItemList.clear()
            ItemType.CARE -> careItemList.clear()
        }
    }

    fun addAllMission(itemData: ItemData) {
        when (itemData.itemType) {
            ItemType.DRUG -> drugItemList.add(itemData)
            ItemType.EVENT -> eventItemList.add(itemData)
            ItemType.MEASURE -> measureItemList.add(itemData)
            ItemType.CARE -> careItemList.add(itemData)
        }
    }

    fun getAllTodoListNum(): Int {
        return drugItemList.size + eventItemList.size + measureItemList.size + careItemList.size
    }

    fun addTimeTitle(timeHeader: Int) {
        timeTitleList.add(timeHeader)
    }

    fun removeTimeTitle(timeHeader: Int) {
        timeTitleList.remove(timeHeader)
    }

    fun clearCurrentList(itemType: ItemType) {
        when (itemType) {
            ItemType.DRUG -> drugCurrentList.clear()
            ItemType.EVENT -> eventCurrentList.clear()
            ItemType.MEASURE -> measureCurrentList.clear()
            ItemType.CARE -> careCurrentList.clear()
        }
    }

    fun arrangeTodoList(): MutableList<ItemDataType> {
        val list = mutableListOf<ItemDataType>()

        timeTitleList.distinct()
        timeTitleList.sort()
        timeTitleList.forEach { time ->
            var num = 0

            timeCurrentList.forEach {
                if ((it as ItemDataType.TimeType).timeInt == time) {
                    list.add(it)
                }
            }

            drugCurrentList.forEach {
                if ((it as ItemDataType.DrugType).timeInt == time) {
                    list.add(it)
                    num +=1
                }
            }

            measureCurrentList.forEach {
                if ((it as ItemDataType.MeasureType).timeInt == time) {
                    list.add(it)
                    num +=1
                }
            }

            eventCurrentList.forEach {
                if ((it as ItemDataType.EventType).timeInt == time) {
                    list.add(it)
                    num +=1
                }
            }

            careCurrentList.forEach {
                if ((it as ItemDataType.CareType).timeInt == time) {
                    list.add(it)
                    num +=1
                }
            }

            if (num == 0){
                list.removeLast()
            }
        }
        return list
    }

}