package com.weiting.tohealth.util

import com.google.firebase.Timestamp
import com.weiting.tohealth.data.*
import com.weiting.tohealth.homepage.ItemDataType
import java.util.*

object ItemArranger {


    fun arrangeTodoList(
        timeTitleList: MutableList<Int>,
        timeCurrentList: MutableList<ItemDataType>,
        drugCurrentList: MutableList<ItemDataType>,
        measureCurrentList: MutableList<ItemDataType>,
        eventCurrentList: MutableList<ItemDataType>,
        careCurrentList: MutableList<ItemDataType>
    ): MutableList<ItemDataType> {
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


    fun isThatDayNeedToDo(itemsData: ItemData, day: Timestamp): Boolean {
        return when (itemsData.itemType) {
            ItemType.DRUG -> {

                return when (itemsData.drugData?.period?.get(TYPE)) {
                    0 -> true
                    1 -> typeSeveralDays(
                        itemsData.drugData.period,
                        itemsData.drugData.startDate ?: Timestamp.now(),
                        day
                    )
                    2 -> typeWeekToDo(itemsData.drugData.period, day)
                    3 -> typeForCycleSetSet(
                        itemsData.drugData.period,
                        itemsData.drugData.startDate ?: Timestamp.now(),
                        day
                    )
                    4 -> false
                    else -> false
                }
            }
            ItemType.CARE -> {

                return when (itemsData.careData?.period?.get(TYPE)) {
                    0 -> true
                    1 -> typeSeveralDays(
                        itemsData.careData.period,
                        itemsData.careData.startDate ?: Timestamp.now(),
                        day
                    )
                    2 -> typeWeekToDo(itemsData.careData.period, day)
                    3 -> typeForCycleSetSet(
                        itemsData.careData.period,
                        itemsData.careData.startDate ?: Timestamp.now(),
                        day
                    )
                    4 -> false
                    else -> false
                }
            }

            ItemType.EVENT -> {

                return when (itemsData.eventData?.period?.get(TYPE)) {
                    0 -> true
                    1 -> typeSeveralDays(
                        itemsData.eventData.period,
                        itemsData.eventData.startDate ?: Timestamp.now(),
                        day
                    )
                    2 -> typeWeekToDo(
                        itemsData.eventData.period,
                        day
                    )
                    3 -> typeForCycleSetSet(
                        itemsData.eventData.period,
                        itemsData.eventData.startDate ?: Timestamp.now(),
                        day
                    )
                    4 -> false
                    else -> false
                }
            }
            else -> true
        }
    }

    private fun typeSeveralDays(
        data: Map<String, Int?>,
        startDate: Timestamp,
        day: Timestamp
    ): Boolean {
        val c = Calendar.getInstance()
        val cNow = Calendar.getInstance()
        c.time = startDate.toDate()
        cNow.time = day.toDate()

        val nowDay = cNow.get(Calendar.DAY_OF_YEAR)
        val startDay = c.get(Calendar.DAY_OF_YEAR)
        val perDay = data[N] ?: 0

        return when ((nowDay - startDay) % perDay == 0) {
            true -> true
            false -> false
        }
    }

    private fun typeWeekToDo(
        data: Map<String, Int?>,
        day: Timestamp
    ): Boolean {
        val c = Calendar.getInstance(Locale.TAIWAN)
        c.time = day.toDate()

        // 0 -> Monday
        val weekDay = c.get(Calendar.DAY_OF_WEEK) - 2
        val perWeek = data[N] ?: 0

        return when (perWeek == weekDay) {
            true -> true
            false -> false
        }
    }

    private fun typeForCycleSetSet(
        data: Map<String, Int?>,
        startDate: Timestamp,
        day: Timestamp
    ): Boolean {
        val suspendDay = data[X] ?: 0
        val onGoingDay = data[N] ?: 0

        val cNow = Calendar.getInstance()
        val c = Calendar.getInstance()
        cNow.time = day.toDate()
        c.time = startDate.toDate()
        val nowDay = cNow.get(Calendar.DAY_OF_YEAR)
        val startDay = c.get(Calendar.DAY_OF_YEAR)
        val oneCycleDay = suspendDay + onGoingDay

        val days = (nowDay - startDay) % oneCycleDay

        return when (days - onGoingDay < 0) {
            true -> true
            false -> false
        }
    }
}
