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
        val itemList = mutableListOf<ItemsDataType>()

        Log.i("startWork", "checkTodayUnCheckedLogs")

        val c = Calendar.getInstance()
        c.time = Timestamp.now().toDate()
        c.add(Calendar.DATE, -1)
        val yesterday = c.time

        val drugList = firebaseDataRepository.getAllDrugs(userId).filter {
            ItemArranger().isThatDayNeedToDo(
                ItemType.DRUG,
                ItemData(DrugData = it),
                Timestamp(yesterday)
            )
        }

        drugList.forEach {
            it.drugLogs =
                firebaseDataRepository.getDrugRecord(it.id ?: "", Timestamp.now()).filter {
                    getTimeStampToDateInt(
                        it.createdTime ?: Timestamp.now()
                    ) == getTimeStampToDateInt(Timestamp(yesterday))
                }
            itemList.add(it)
        }

        val measureList = firebaseDataRepository.getAllMeasures(userId).filter {
            ItemArranger().isThatDayNeedToDo(
                ItemType.MEASURE,
                ItemData(MeasureData = it),
                Timestamp(yesterday)
            )
        }
        measureList.forEach {
            it.measureLogs.filter {
                getTimeStampToDateInt(it.createdTime ?: Timestamp.now()) == getTimeStampToDateInt(
                    Timestamp(yesterday)
                )
            }
            itemList.add(it)
        }

        val careList = firebaseDataRepository.getAllCares(userId).filter {
            ItemArranger().isThatDayNeedToDo(
                ItemType.CARE,
                ItemData(CareData = it),
                Timestamp(yesterday)
            )
        }
        careList.forEach {
            it.careLogs =
                firebaseDataRepository.getCareRecord(it.id ?: "", Timestamp.now()).filter {
                    getTimeStampToDateInt(it.createdTime ?: Timestamp.now()) == getTimeStampToDateInt(
                        Timestamp(yesterday)
                    )
                }
            itemList.add(it)
        }

        val activityList = firebaseDataRepository.getAllActivities(userId).filter {
            ItemArranger().isThatDayNeedToDo(
                ItemType.ACTIVITY,
                ItemData(ActivityData = it),
                Timestamp(yesterday)
            )
        }
        activityList.forEach {
            it.activityLogs =
                firebaseDataRepository.getActivityRecord(it.id ?: "", Timestamp.now()).filter {
                    getTimeStampToDateInt(it.createdTime ?: Timestamp.now()) == getTimeStampToDateInt(
                        Timestamp(yesterday)
                    )
                }
            itemList.add(it)
        }

        getUncheckLogs(itemList, firebaseDataRepository)
    }

    private suspend fun getUncheckLogs(
        itemList: List<ItemsDataType>,
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
            when (itemData) {
                is Drug -> {
                    itemData.drugLogs.forEach { drugLog ->
                        timeTags.add(drugLog.timeTag ?: 0)
                    }
                    itemData.executedTime.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postDrugRecord(
                                itemData.id ?: "",
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

                is Activity -> {
                    itemData.activityLogs.forEach { activityLog ->
                        timeTags.add(activityLog.timeTag ?: 0)
                    }
                    itemData.executedTime.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postActivityRecord(
                                itemData.id ?: "",
                                ActivityLog(
                                    timeTag = getTimeStampToTimeInt(timeStamp),
                                    result = 3,
                                    createdTime = time,
                                )
                            )
                        }
                    }
                    timeTags.clear()
                }

                is Measure -> {
                    itemData.measureLogs.forEach { measureLog ->
                        timeTags.add(measureLog.timeTag ?: 0)
                    }
                    itemData.executedTime.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postMeasureRecord(
                                itemData.id ?: "",
                                MeasureLog(
                                    id = firebaseDataRepository.getMeasureRecordId(itemData.id ?: ""),
                                    timeTag = getTimeStampToTimeInt(timeStamp),
                                    result = 3,
                                    createdTime = time
                                )
                            )
                        }
                    }
                    timeTags.clear()
                }

                is Care -> {
                    itemData.careLogs.forEach { careLog ->
                        timeTags.add(careLog.timeTag ?: 0)
                    }
                    itemData.executeTime.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postCareRecord(
                                itemData.id ?: "",
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
