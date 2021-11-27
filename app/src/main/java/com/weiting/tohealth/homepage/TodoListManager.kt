package com.weiting.tohealth.homepage

import com.weiting.tohealth.data.*

class TodoListManager {

    /*
        Store the all todolist item below.
     */
    private val drugItemList = mutableListOf<ItemData>()
    private val measureItemList = mutableListOf<ItemData>()
    private val eventItemList = mutableListOf<ItemData>()
    private val careItemList = mutableListOf<ItemData>()

    /*
        Store the unchecked todolist item below.
     */
    private val drugCurrentList = mutableListOf<ItemDataType>()
    private val measureCurrentList = mutableListOf<ItemDataType>()
    private val eventCurrentList = mutableListOf<ItemDataType>()
    private val careCurrentList = mutableListOf<ItemDataType>()

    private val timeCurrentList = mutableListOf<ItemDataType>()
    val timeTitleList = mutableListOf<Int>()

    fun getCurrentItemDataType(itemDataType: ItemDataType) {
        when (itemDataType) {
            is ItemDataType.TimeType -> timeCurrentList.add(itemDataType)
            is ItemDataType.CareType -> careCurrentList.add(itemDataType)
            is ItemDataType.DrugType -> drugCurrentList.add(itemDataType)
            is ItemDataType.EventType -> eventCurrentList.add(itemDataType)
            is ItemDataType.MeasureType -> measureCurrentList.add(itemDataType)
        }
    }

    fun clearAllItemList(itemType: ItemType){
        when(itemType){
            ItemType.DRUG -> drugItemList.clear()
            ItemType.EVENT -> eventItemList.clear()
            ItemType.MEASURE -> measureItemList.clear()
            ItemType.CARE -> careItemList.clear()
        }
    }

    fun getAllItem(itemData: ItemData) {
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

    fun getTimeTitle(timeHeader: Int) {
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

            timeCurrentList.forEach {
                if ((it as ItemDataType.TimeType).timeInt == time) {
                    list.add(it)
                }
            }

            drugCurrentList.forEach {
                if ((it as ItemDataType.DrugType).timeInt == time) {
                    list.add(it)
                }
            }

            measureCurrentList.forEach {
                if ((it as ItemDataType.MeasureType).timeInt == time) {
                    list.add(it)
                }
            }

            eventCurrentList.forEach {
                if ((it as ItemDataType.EventType).timeInt == time) {
                    list.add(it)
                }
            }

            careCurrentList.forEach {
                if ((it as ItemDataType.CareType).timeInt == time) {
                    list.add(it)
                }
            }
        }
        return list
    }

}