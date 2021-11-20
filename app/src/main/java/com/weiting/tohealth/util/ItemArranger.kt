package com.weiting.tohealth.util

import android.util.Log
import android.util.TimeUtils
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.data.ItemType
import java.util.*
import java.util.concurrent.TimeUnit

class ItemArranger() {

    fun isThatDayNeedToDo(itemType: ItemType, itemData: ItemData, day: Timestamp): Boolean {

        return when (itemType) {
            ItemType.DRUG -> {

                return when (itemData.DrugData?.period?.get("type")) {
                    0 -> true
                    1 -> typeSeveralDays(
                        itemData.DrugData.period,
                        itemData.DrugData.startDate!!,
                        day
                    )
                    2 -> typeWeekToDo(itemData.DrugData.period, itemData.DrugData.startDate!!, day)
                    3 -> typeForCycleSetSet(
                        itemData.DrugData.period,
                        itemData.DrugData.startDate!!,
                        day
                    )
                    4 -> false
                    else -> false
                }

            }
            ItemType.CARE -> {

                return when (itemData.CareData?.period?.get("type")) {
                    0 -> true
                    1 -> typeSeveralDays(
                        itemData.CareData.period,
                        itemData.CareData.startDate!!,
                        day
                    )
                    2 -> typeWeekToDo(itemData.CareData.period, itemData.CareData.startDate!!, day)
                    3 -> typeForCycleSetSet(
                        itemData.CareData.period,
                        itemData.CareData.startDate!!,
                        day
                    )
                    4 -> false
                    else -> false
                }
            }

            ItemType.ACTIVITY -> {

                return when (itemData.ActivityData?.period?.get("type")) {
                    0 -> true
                    1 -> typeSeveralDays(
                        itemData.ActivityData.period,
                        itemData.ActivityData.startDate!!,
                        day
                    )
                    2 -> typeWeekToDo(
                        itemData.ActivityData.period,
                        itemData.ActivityData.startDate!!,
                        day
                    )
                    3 -> typeForCycleSetSet(
                        itemData.ActivityData.period,
                        itemData.ActivityData.startDate!!,
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
        startDate: Timestamp,
        day: Timestamp
    ): Boolean {
        val c = Calendar.getInstance(Locale.TAIWAN)
        c.time = day.toDate()

        // 0 -> Monday
        val weekDay = c.get(Calendar.DAY_OF_WEEK) - 2
//        Log.i("Taiwan", weekDay.toString())
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