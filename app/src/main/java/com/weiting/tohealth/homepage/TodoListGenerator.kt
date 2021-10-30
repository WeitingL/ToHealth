package com.weiting.tohealth.homepage

import android.util.Log
import com.weiting.tohealth.data.*
import com.weiting.tohealth.homepage.HomeViewModel.*
import com.weiting.tohealth.toTimeFromTimeStamp
import java.util.*

class TodoListGenerator() {

    private val timeIntList = mutableListOf<Int>()
    private val timeList = mutableListOf<ItemDataType>()

    private val drugItemList = mutableListOf<ItemDataType>()
    private val measureItemList = mutableListOf<ItemDataType>()
    private val activityItemList = mutableListOf<ItemDataType>()
    private val careItemList = mutableListOf<ItemDataType>()

    var logList = mutableListOf<ItemLogData>()
    var outDateTimeHeader = mutableListOf<ItemDataType>()

//    private val drugLogList = mutableListOf<ItemLogData>()
//    private val measureLogList = mutableListOf<ItemLogData>()
//    private val activityLogList = mutableListOf<ItemLogData>()
//    private val careLogList = mutableListOf<ItemLogData>()

    //TodoList Final items.
    var finalItemList = mutableListOf<ItemDataType>()
    var outDateItemList = mutableListOf<ItemDataType>()

    fun getDrugs(list: List<Drug>) {
        drugItemList.clear()
        list.forEach { drug ->
            drug.executeTime.forEach {
                val c = Calendar.getInstance()
                c.time = it.toDate()
                val timeInt = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE)

                drugItemList.add(ItemDataType.DrugType(ItemData(DrugData = drug), timeInt))

                if (timeInt !in timeIntList) {
                    timeList.add(ItemDataType.TimeType(toTimeFromTimeStamp(it), timeInt))
                    timeIntList.add(timeInt)
                    timeIntList.sort()
                }
            }
        }
        reArrangeItemDataType()
    }

    fun getMeasures(list: List<Measure>) {
        measureItemList.clear()
        list.forEach { measure ->

            measure.executeTime.forEach {
                val c = Calendar.getInstance()
                c.time = it.toDate()
                val timeInt = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE)

                measureItemList.add(
                    ItemDataType.MeasureType(
                        ItemData(MeasureData = measure),
                        timeInt
                    )
                )

                if (timeInt !in timeIntList) {
                    timeList.add(ItemDataType.TimeType(toTimeFromTimeStamp(it), timeInt))
                    timeIntList.add(timeInt)
                    timeIntList.sort()
                }
            }
        }
        reArrangeItemDataType()
    }

    fun getActivity(list: List<Activity>) {
        activityItemList.clear()
        list.forEach { activity ->

            activity.executeTime.forEach {
                val c = Calendar.getInstance()
                c.time = it.toDate()
                val timeInt = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE)

                activityItemList.add(
                    ItemDataType.ActivityType(
                        ItemData(ActivityData = activity),
                        timeInt
                    )
                )

                if (timeInt !in timeIntList) {
                    timeList.add(ItemDataType.TimeType(toTimeFromTimeStamp(it), timeInt))
                    timeIntList.add(timeInt)
                    timeIntList.sort()
                }
            }
        }
        reArrangeItemDataType()
    }

    fun getCares(list: List<Care>) {
        careItemList.clear()
        list.forEach { care ->
            care.executeTime.forEach {
                val c = Calendar.getInstance()
                c.time = it.toDate()
                val timeInt = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE)

                careItemList.add(ItemDataType.CareType(ItemData(CareData = care), timeInt))

                if (timeInt !in timeIntList) {
                    timeList.add(ItemDataType.TimeType(toTimeFromTimeStamp(it), timeInt))
                    timeIntList.add(timeInt)
                    timeIntList.sort()
                }
            }
        }
        reArrangeItemDataType()
    }

    private fun reArrangeItemDataType() {
        finalItemList.clear()

        timeIntList.forEach { intTime ->
            timeList.forEach { timeItem ->
                if ((timeItem as ItemDataType.TimeType).timeInt == intTime) {
                    finalItemList.add(timeItem)

                    drugItemList.forEach { durg ->
                        if ((durg as ItemDataType.DrugType).timeInt == intTime) {
                            if (durg.timeInt == intTime) {
                                finalItemList.add(durg)
                            }
                        }
                    }

                    measureItemList.forEach { measure ->
                        if ((measure as ItemDataType.MeasureType).timeInt == intTime) {
                            if (measure.timeInt == intTime) {
                                finalItemList.add(measure)
                            }
                        }
                    }

                    activityItemList.forEach { activity ->
                        if ((activity as ItemDataType.ActivityType).timeInt == intTime) {
                            if (activity.timeInt == intTime) {
                                finalItemList.add(activity)
                            }
                        }
                    }

                    careItemList.forEach { care ->
                        if ((care as ItemDataType.CareType).timeInt == intTime) {
                            if (care.timeInt == intTime) {
                                finalItemList.add(care)
                            }
                        }
                    }
                }
            }
        }
    }

    fun getOutTimeHeader(itemDataType: ItemDataType, position: Int){
        outDateTimeHeader += itemDataType
    }

    fun getLog(itemLogData: ItemLogData, position: Int) {
        logList += itemLogData

        if (outDateTimeHeader.isNotEmpty()){
            outDateItemList = finalItemList
            finalItemList.removeAt(position)
            finalItemList.removeAt(position - 1)
        }else{
            outDateItemList = finalItemList
            finalItemList.removeAt(position)
        }
    }

    fun removeLastLog(){
        if (logList.isNotEmpty()){
           logList.removeLast()
        }

        finalItemList = outDateItemList
    }
}