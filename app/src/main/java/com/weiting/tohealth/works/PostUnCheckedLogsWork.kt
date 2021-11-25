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

        val drugList = firebaseDataRepository.getAllDrugs(userId).filter {
            ItemArranger().isThatDayNeedToDo(ItemData(DrugData = it), Timestamp(yesterday))
        }

        drugList.forEach {
            it.drugLogs =
                firebaseDataRepository.getDrugRecord(it.id ?: "").filter {
                    getTimeStampToDateInt(
                        it.createdTime ?: Timestamp.now()
                    ) == getTimeStampToDateInt(Timestamp(yesterday))
                }
            itemList.add(ItemData(DrugData = it))
        }

        val measureList = firebaseDataRepository.getAllMeasures(userId).filter {
            ItemArranger().isThatDayNeedToDo(ItemData(MeasureData =it), Timestamp(yesterday))
        }
        measureList.forEach {
            it.measureLogs.filter {
                getTimeStampToDateInt(it.createdTime ?: Timestamp.now()) == getTimeStampToDateInt(
                    Timestamp(yesterday)
                )
            }
            itemList.add(ItemData(MeasureData =it))
        }

        val careList = firebaseDataRepository.getAllCares(userId).filter {
            ItemArranger().isThatDayNeedToDo(ItemData(CareData =it), Timestamp(yesterday))
        }
        careList.forEach {
            it.careLogs =
                firebaseDataRepository.getCareRecord(it.id ?: "").filter {
                    getTimeStampToDateInt(
                        it.createdTime ?: Timestamp.now()
                    ) == getTimeStampToDateInt(
                        Timestamp(yesterday)
                    )
                }
            itemList.add(ItemData(CareData =it))
        }

        val activityList = firebaseDataRepository.getAllActivities(userId).filter {
            ItemArranger().isThatDayNeedToDo(ItemData(EventData =it), Timestamp(yesterday))
        }
        activityList.forEach {
            it.eventLogs =
                firebaseDataRepository.getActivityRecord(it.id ?: "").filter {
                    getTimeStampToDateInt(
                        it.createdTime ?: Timestamp.now()
                    ) == getTimeStampToDateInt(
                        Timestamp(yesterday)
                    )
                }
            itemList.add(ItemData(EventData =it))
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
                    itemData.DrugData?.drugLogs?.forEach { drugLog ->
                        timeTags.add(drugLog.timeTag ?: 0)
                    }
                    itemData.DrugData?.executedTime?.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postDrugRecord(
                                itemData.DrugData.id ?: "",
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

                ItemType.EVENT-> {
                    itemData.EventData?.eventLogs?.forEach { activityLog ->
                        timeTags.add(activityLog.timeTag ?: 0)
                    }
                    itemData.EventData?.executedTime?.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postActivityRecord(
                                itemData.EventData.id ?: "",
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
                    itemData.MeasureData?.measureLogs?.forEach { measureLog ->
                        timeTags.add(measureLog.timeTag ?: 0)
                    }
                    itemData.MeasureData?.executedTime?.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postMeasureRecord(
                                itemData.MeasureData.id ?: "",
                                MeasureLog(
                                    id = firebaseDataRepository.getMeasureRecordId(
                                        itemData.MeasureData.id ?: ""
                                    ),
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
                    itemData.CareData?.careLogs?.forEach { careLog ->
                        timeTags.add(careLog.timeTag ?: 0)
                    }
                    itemData.CareData?.executeTime?.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postCareRecord(
                                itemData.CareData.id ?: "",
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
