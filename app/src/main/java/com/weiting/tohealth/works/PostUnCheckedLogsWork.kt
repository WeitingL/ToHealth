package com.weiting.tohealth.works

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.data.*
import com.weiting.tohealth.util.ItemArranger
import com.weiting.tohealth.util.Util.getTimeStampToDateInt
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt
import java.util.*

class PostUnCheckedLogsWork {

    /*
        Check today undo item and post the undo log ->
        apply the new alarm to alarm manager.
     */

    suspend fun checkTodayUnCheckedLogs(firebaseDataRepository: FirebaseRepository) {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        val itemList = mutableListOf<ItemData>()

        Log.i("startWork", "checkTodayUnCheckedLogs")

        val c = Calendar.getInstance()
        c.time = Timestamp.now().toDate()
        c.add(Calendar.DATE, -1)
        val yesterday = c.time

        val drugs = when (val result = firebaseDataRepository.getAllDrugs(userId)) {
            is Result.Success -> result.data
            else -> listOf()
        }
        val drugList = drugs.filter {
            ItemArranger.isThatDayNeedToDo(ItemData(drugData = it), Timestamp(yesterday))
        }

        drugList.forEach {
            val logs = when (val result = firebaseDataRepository.getDrugLogs(it.id ?: "")) {
                is Result.Success -> result.data
                else -> listOf()
            }
            it.drugLogs = logs.filter {
                getTimeStampToDateInt(
                    it.createdTime ?: Timestamp.now()
                ) == getTimeStampToDateInt(Timestamp(yesterday))
            }
            itemList.add(ItemData(drugData = it))
        }

        val measures = when (val result = firebaseDataRepository.getAllMeasures(userId)) {
            is Result.Success -> result.data
            else -> listOf()
        }
        val measureList = measures.filter {
            ItemArranger.isThatDayNeedToDo(ItemData(measureData = it), Timestamp(yesterday))
        }
        measureList.forEach {
            it.measureLogs.filter {
                getTimeStampToDateInt(it.createdTime ?: Timestamp.now()) == getTimeStampToDateInt(
                    Timestamp(yesterday)
                )
            }
            itemList.add(ItemData(measureData = it))
        }

        val cares = when (val result = firebaseDataRepository.getAllCares(userId)) {
            is Result.Success -> result.data
            else -> listOf()
        }
        val careList = cares.filter {
            ItemArranger.isThatDayNeedToDo(ItemData(careData = it), Timestamp(yesterday))
        }
        careList.forEach {
            val logs = when (val result = firebaseDataRepository.getCareLogs(it.id ?: "")) {
                is Result.Success -> result.data
                else -> listOf()
            }
            it.careLogs = logs.filter {
                getTimeStampToDateInt(
                    it.createdTime ?: Timestamp.now()
                ) == getTimeStampToDateInt(
                    Timestamp(yesterday)
                )
            }
            itemList.add(ItemData(careData = it))
        }

        val events = when (val result = firebaseDataRepository.getAllEvents(userId)) {
            is Result.Success -> result.data
            else -> listOf()
        }
        val eventList = events.filter {
            ItemArranger.isThatDayNeedToDo(ItemData(eventData = it), Timestamp(yesterday))
        }
        eventList.forEach {
            val logs = when (val result = firebaseDataRepository.getEventLogs(it.id ?: "")) {
                is Result.Success -> result.data
                else -> listOf()
            }
            it.eventLogs = logs.filter {
                getTimeStampToDateInt(
                    it.createdTime ?: Timestamp.now()
                ) == getTimeStampToDateInt(
                    Timestamp(yesterday)
                )
            }
            itemList.add(ItemData(eventData = it))
        }

        getUncheckLogs(itemList, firebaseDataRepository)
    }

    private suspend fun getUncheckLogs(
        itemList: List<ItemData>,
        firebaseDataRepository: FirebaseRepository
    ) {
        val timeTags = mutableListOf<Int>()
        val c = Calendar.getInstance()
        c.time = Timestamp.now().toDate()
        c.add(Calendar.DATE, -1)
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        val time = Timestamp(c.time)

        itemList.forEach { itemData ->
            when (itemData.itemType) {
                ItemType.DRUG -> {
                    itemData.drugData?.drugLogs?.forEach { drugLog ->
                        timeTags.add(drugLog.timeTag ?: 0)
                    }
                    itemData.drugData?.executedTime?.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postDrugLog(
                                itemData.drugData.id ?: "",
                                DrugLog(
                                    timeTag = getTimeStampToTimeInt(timeStamp),
                                    result = 3,
                                    createdTime = time
                                )
                            )
                        }
                    }
                    timeTags.clear()
                }

                ItemType.EVENT -> {
                    itemData.eventData?.eventLogs?.forEach { activityLog ->
                        timeTags.add(activityLog.timeTag ?: 0)
                    }
                    itemData.eventData?.executedTime?.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postEventLog(
                                itemData.eventData.id ?: "",
                                EventLog(
                                    timeTag = getTimeStampToTimeInt(timeStamp),
                                    result = 3,
                                    createdTime = time,
                                )
                            )
                        }
                    }
                    timeTags.clear()
                }

                ItemType.MEASURE -> {
                    itemData.measureData?.measureLogs?.forEach { measureLog ->
                        timeTags.add(measureLog.timeTag ?: 0)
                    }
                    itemData.measureData?.executedTime?.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            val id = when (val result = firebaseDataRepository.getMeasureLogId(
                                itemData.measureData.id ?: ""
                            )) {
                                is Result.Success -> result.data
                                else -> null
                            }

                            firebaseDataRepository.postMeasureLog(
                                itemData.measureData.id ?: "",
                                MeasureLog(
                                    id = id,
                                    timeTag = getTimeStampToTimeInt(timeStamp),
                                    result = 3,
                                    createdTime = time
                                )
                            )
                        }
                    }
                    timeTags.clear()
                }

                ItemType.CARE -> {
                    itemData.careData?.careLogs?.forEach { careLog ->
                        timeTags.add(careLog.timeTag ?: 0)
                    }
                    itemData.careData?.executedTime?.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postCareLog(
                                itemData.careData.id ?: "",
                                CareLog(
                                    timeTag = getTimeStampToTimeInt(timeStamp),
                                    result = 3,
                                    createdTime = time
                                )
                            )
                        }
                    }
                    timeTags.clear()
                }
            }
        }
    }
}
