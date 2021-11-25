package com.weiting.tohealth.util

import com.google.firebase.Timestamp
import com.weiting.tohealth.data.*
import java.util.*

class ItemArranger {



    fun isThatDayNeedToDo(itemsData: ItemData, day: Timestamp): Boolean {

        return when (itemsData.itemType) {
            ItemType.DRUG -> {

                return when (itemsData.DrugData?.period?.get("type")) {
                    0 -> true
                    1 -> typeSeveralDays(
                        itemsData.DrugData.period,
                        itemsData.DrugData.startDate ?: Timestamp.now(),
                        day
                    )
                    2 -> typeWeekToDo(itemsData.DrugData.period, day)
                    3 -> typeForCycleSetSet(
                        itemsData.DrugData.period,
                        itemsData.DrugData.startDate ?: Timestamp.now(),
                        day
                    )
                    4 -> false
                    else -> false
                }
            }
            ItemType.CARE -> {

                return when (itemsData.CareData?.period?.get("type")) {
                    0 -> true
                    1 -> typeSeveralDays(
                        itemsData.CareData.period,
                        itemsData.CareData.startDate ?: Timestamp.now(),
                        day
                    )
                    2 -> typeWeekToDo(itemsData.CareData.period, day)
                    3 -> typeForCycleSetSet(
                        itemsData.CareData.period,
                        itemsData.CareData.startDate ?: Timestamp.now(),
                        day
                    )
                    4 -> false
                    else -> false
                }
            }

            ItemType.EVENT -> {

                return when (itemsData.EventData?.period?.get("type")) {
                    0 -> true
                    1 -> typeSeveralDays(
                        itemsData.EventData.period,
                        itemsData.EventData.startDate ?: Timestamp.now(),
                        day
                    )
                    2 -> typeWeekToDo(
                        itemsData.EventData.period,
                        day
                    )
                    3 -> typeForCycleSetSet(
                        itemsData.EventData.period,
                        itemsData.EventData.startDate ?: Timestamp.now(),
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
        val perDay = data["N"]!!

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
        val perWeek = data["N"]!!

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
        val suspendDay = data["X"]!!
        val onGoingDay = data["N"]!!

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
