package com.weiting.tohealth.works

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.data.*
import com.weiting.tohealth.getTimeStampToDateInt
import com.weiting.tohealth.getTimeStampToTimeInt
import com.weiting.tohealth.util.ItemArranger

class PostUnCheckedLogsWork() {

    /*
        Check today undo item and post the undo log ->
        apply the new alarm to alarm manager.
     */

    suspend fun checkTodayUnCheckedLogs(firebaseDataRepository: FirebaseRepository) {
        val user = Firebase.auth.currentUser?.uid
        val itemList = mutableListOf<ItemsDataType>()

        val drugList = firebaseDataRepository.getAllDrugs(user!!).filter {
            ItemArranger().isTodayNeedToDo(ItemType.DRUG, ItemData(DrugData = it))
        }

        drugList.forEach {
            it.drugLogs =
                firebaseDataRepository.getDrugRecord(it.id!!, Timestamp.now()).filter {
                    getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(Timestamp.now())
                }
            itemList.add(it)
        }

        val measureList = firebaseDataRepository.getAllMeasures(user).filter {
            ItemArranger().isTodayNeedToDo(ItemType.MEASURE, ItemData(MeasureData = it))
        }
        measureList.forEach {
            it.measureLogs.filter {
                getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(
                    Timestamp.now()
                )
            }
            itemList.add(it)
        }

        val careList = firebaseDataRepository.getAllCares(user).filter {
            ItemArranger().isTodayNeedToDo(ItemType.CARE, ItemData(CareData = it))
        }
        careList.forEach {
            it.careLogs =
                firebaseDataRepository.getCareRecord(it.id!!, Timestamp.now()).filter {
                    getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(
                        Timestamp.now()
                    )
                }
            itemList.add(it)
        }

        val activityList = firebaseDataRepository.getAllActivities(user).filter {
            ItemArranger().isTodayNeedToDo(ItemType.ACTIVITY, ItemData(ActivityData = it))
        }
        activityList.forEach {
            it.activityLogs =
                firebaseDataRepository.getActivityRecord(it.id!!, Timestamp.now()).filter {
                    getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(
                        Timestamp.now()
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

        itemList.forEach { itemData ->
            when (itemData) {
                is Drug -> {
                    itemData.drugLogs.forEach { drugLog ->
                        timeTags.add(drugLog.timeTag!!)
                    }
                    itemData.executedTime.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postDrugRecord(
                                itemData.id!!, DrugLog(
                                    timeTag = getTimeStampToTimeInt(timeStamp),
                                    result = 3,
                                    createdTime = Timestamp.now()
                                )
                            )
                        }
                    }
                    timeTags.clear()
                }

                is Activity -> {
                    itemData.activityLogs.forEach { activityLog ->
                        timeTags.add(activityLog.timeTag!!)
                    }
                    itemData.executedTime.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postActivityRecord(
                                itemData.id!!, ActivityLog(
                                    timeTag = getTimeStampToTimeInt(timeStamp),
                                    result = 3,
                                    createdTime = Timestamp.now(),
                                )
                            )
                        }
                    }
                    timeTags.clear()
                }

                is Measure -> {
                    itemData.measureLogs.forEach { measureLog ->
                        timeTags.add(measureLog.timeTag!!)
                    }
                    itemData.executedTime.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postMeasureRecord(
                                itemData.id!!,
                                MeasureLog(
                                    id = firebaseDataRepository.getMeasureRecordId(itemData.id!!),
                                    timeTag = getTimeStampToTimeInt(timeStamp),
                                    result = 3,
                                    createdTime = Timestamp.now()
                                )
                            )
                        }
                    }
                    timeTags.clear()
                }

                is Care -> {
                    itemData.careLogs.forEach { careLog ->
                        timeTags.add(careLog.timeTag!!)
                    }
                    itemData.executeTime.forEach { timeStamp ->
                        if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                            firebaseDataRepository.postCareRecord(
                                itemData.id!!,
                                CareLog(
                                    timeTag = getTimeStampToTimeInt(timeStamp),
                                    result = 3,
                                    createdTime = Timestamp.now()
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