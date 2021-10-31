package com.weiting.tohealth.homepage

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.*
import com.weiting.tohealth.getTimeStampToDateInt
import com.weiting.tohealth.getTimeStampToTimeInt
import com.weiting.tohealth.toTimeFromTimeStamp
import kotlinx.coroutines.*

class HomeViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    /*
        Logic about todoList after get the livedata from firebase.
        Totally 4 LiveData are needed order by time(hour and min).

        Identify the finished mission by numbers and created time of ItemLog.
     */

    private val drugList = firebaseDataRepository.getLiveDrugList(UserManager.userId)
    private val measureList = firebaseDataRepository.getLiveMeasureList(UserManager.userId)
    private val activityList = firebaseDataRepository.getLiveActivityList(UserManager.userId)
    private val careList = firebaseDataRepository.getLiveCareList(UserManager.userId)

    private val drugCurrentList = mutableListOf<ItemDataType>()
    private val measureCurrentList = mutableListOf<ItemDataType>()
    private val activityCurrentList = mutableListOf<ItemDataType>()
    private val careCurrentList = mutableListOf<ItemDataType>()
    private val timeCurrentList = mutableListOf<ItemDataType>()
    private val timeIntList = mutableListOf<Int>()

    val itemDataMediator = MediatorLiveData<MutableList<ItemDataType>>().apply {

        addSource(drugList) { drugList ->
            drugCurrentList.clear()
            drugList.forEach { drug ->

                viewModelScope.launch {
                    drug.drugLogs = firebaseDataRepository.getDrugRecord(drug.id!!, Timestamp.now())
                }

                val todayLogCreateTimeIntList = mutableListOf<Int>()
                drug.drugLogs.forEach { drugLog ->
                    if (getTimeStampToDateInt(drugLog.createTime!!) ==
                        getTimeStampToDateInt(Timestamp.now())
                    ) {
                        todayLogCreateTimeIntList.add(getTimeStampToDateInt(drugLog.createTime))
                    }
                    todayLogCreateTimeIntList.sort()
                }

                /*
                    There are three situation:
                    LogList is empty -> all tasks create.
                    ExecuteTime only one -> Just check the LogList.
                    multiple execute time -> check is the logtime before the next execute time?
                 */

//                if (todayLogCreateTimeIntList.isEmpty()) {
//                    drug.executeTime.forEach {
//                        drugCurrentList.add(
//                            ItemDataType.DrugType(
//                                ItemData(DrugData = drug),
//                                getTimeStampToTimeInt(it)
//                            )
//                        )
//                        if (getTimeStampToTimeInt(it) !in timeIntList) {
//                            timeIntList.add(getTimeStampToTimeInt(it))
//                            timeCurrentList.add(
//                                ItemDataType.TimeType(
//                                    toTimeFromTimeStamp(it),
//                                    getTimeStampToTimeInt(it)
//                                )
//                            )
//                        }
//                    }
//                } else if (drug.executeTime.size == 1 && todayLogCreateTimeIntList.isEmpty()) {
//
//                    drugCurrentList.add(
//                        ItemDataType.DrugType(
//                            ItemData(DrugData = drug),
//                            getTimeStampToTimeInt(drug.executeTime.first())
//                        )
//                    )
//                    if (getTimeStampToTimeInt(drug.executeTime.first()) !in timeIntList) {
//                        timeIntList.add(getTimeStampToTimeInt(drug.executeTime.first()))
//                        timeCurrentList.add(
//                            ItemDataType.TimeType(
//                                toTimeFromTimeStamp(drug.executeTime.first()),
//                                getTimeStampToTimeInt(drug.executeTime.first())
//                            )
//                        )
//                    }
//                } else {
//
//                    /*
//                    There there situation:
//                    execute time < Now ->
//                    execute time > Now ->
//                     */
//
//                }

                drug.executeTime.forEach {
                    drugCurrentList.add(
                        ItemDataType.DrugType(
                            ItemData(DrugData = drug),
                            getTimeStampToTimeInt(it)
                        )
                    )
                    if (getTimeStampToTimeInt(it) !in timeIntList) {
                        timeIntList.add(getTimeStampToTimeInt(it))
                        timeCurrentList.add(
                            ItemDataType.TimeType(
                                toTimeFromTimeStamp(it),
                                getTimeStampToTimeInt(it)
                            )
                        )
                    }
                }
            }


            value = arrangeTodoList(
                timeIntList,
                timeCurrentList,
                drugCurrentList,
                measureCurrentList,
                activityCurrentList,
                careCurrentList
            )
        }

        addSource(measureList) { measureList ->
            measureCurrentList.clear()
            measureList.forEach { measure ->

                viewModelScope.launch {
                    measure.measureLogs =
                        firebaseDataRepository.getMeasureRecord(measure.id!!, Timestamp.now())
                }

                measure.executeTime.forEach {
                    measureCurrentList.add(
                        ItemDataType.MeasureType(
                            ItemData(MeasureData = measure),
                            getTimeStampToTimeInt(it)
                        )
                    )

                    if (getTimeStampToTimeInt(it) !in timeIntList) {
                        timeIntList.add(getTimeStampToTimeInt(it))
                        timeCurrentList.add(
                            ItemDataType.TimeType(
                                toTimeFromTimeStamp(it),
                                getTimeStampToTimeInt(it)
                            )
                        )
                    }
                }
            }
            value = arrangeTodoList(
                timeIntList,
                timeCurrentList,
                drugCurrentList,
                measureCurrentList,
                activityCurrentList,
                careCurrentList
            )
        }

        addSource(activityList) { activityList ->
            activityCurrentList.clear()
            activityList.forEach { activity ->

                viewModelScope.launch {
                    activity.activityLogs =
                        firebaseDataRepository.getActivityRecord(activity.id!!, Timestamp.now())
                }

                activity.executeTime.forEach {
                    activityCurrentList.add(
                        ItemDataType.ActivityType(
                            ItemData(ActivityData = activity),
                            getTimeStampToTimeInt(it)
                        )
                    )

                    if (getTimeStampToTimeInt(it) !in timeIntList) {
                        timeIntList.add(getTimeStampToTimeInt(it))
                        timeCurrentList.add(
                            ItemDataType.TimeType(
                                toTimeFromTimeStamp(it),
                                getTimeStampToTimeInt(it)
                            )
                        )
                    }
                }
            }
            value = arrangeTodoList(
                timeIntList,
                timeCurrentList,
                drugCurrentList,
                measureCurrentList,
                activityCurrentList,
                careCurrentList
            )
        }

        addSource(careList) { careList ->
            careCurrentList.clear()
            careList.forEach { care ->

                viewModelScope.launch {
                    care.careLogs = firebaseDataRepository.getCareRecord(care.id!!, Timestamp.now())
                }

                care.executeTime.forEach {
                    careCurrentList.add(
                        ItemDataType.CareType(
                            ItemData(CareData = care),
                            getTimeStampToTimeInt(it)
                        )
                    )

                    if (getTimeStampToTimeInt(it) !in timeIntList) {
                        timeIntList.add(getTimeStampToTimeInt(it))
                        timeCurrentList.add(
                            ItemDataType.TimeType(
                                toTimeFromTimeStamp(it),
                                getTimeStampToTimeInt(it)
                            )
                        )
                    }
                }
            }
            value = arrangeTodoList(
                timeIntList,
                timeCurrentList,
                drugCurrentList,
                measureCurrentList,
                activityCurrentList,
                careCurrentList
            )
        }
    }

    private fun arrangeTodoList(
        timeList: MutableList<Int>,
        timeCurrentList: MutableList<ItemDataType>,
        drugList: MutableList<ItemDataType>,
        measureList: MutableList<ItemDataType>,
        activityList: MutableList<ItemDataType>,
        careList: MutableList<ItemDataType>
    ): MutableList<ItemDataType> {
        val list = mutableListOf<ItemDataType>()

        timeList.distinct()
        timeList.sort()
        timeList.forEach { time ->

            timeCurrentList.forEach {
                if ((it as ItemDataType.TimeType).timeInt == time) {
                    list.add(it)
                }
            }

            drugList.forEach {
                if ((it as ItemDataType.DrugType).timeInt == time) {
                    list.add(it)
                }
            }

            measureList.forEach {
                if ((it as ItemDataType.MeasureType).timeInt == time) {
                    list.add(it)
                }
            }

            activityList.forEach {
                if ((it as ItemDataType.ActivityType).timeInt == time) {
                    list.add(it)
                }
            }

            careList.forEach {
                if ((it as ItemDataType.CareType).timeInt == time) {
                    list.add(it)
                }
            }

        }
        return list
    }

    /*
       Swipe to Skip
     */

    private val skipList = mutableListOf<SwipeData>()

    fun swipeToSkip(swipeData: SwipeData) {
        skipList.add(swipeData)
    }

    fun undoSwipeToSkip() {
        val lastData = skipList.last()
        skipList.removeLast()

        val currentList = itemDataMediator.value
        currentList?.add(lastData.position, lastData.itemDataType)
        itemDataMediator.value = currentList
    }

    fun postSkipLog() {
        skipList.forEach {
            when (it.itemDataType) {
                is ItemDataType.DrugType -> {
                    firebaseDataRepository.postDrugRecord(
                        it.itemDataType.drug.DrugData?.id!!, DrugLog(
                            result = 1,
                            createTime = Timestamp.now()
                        )
                    )
                }

                is ItemDataType.MeasureType -> {
                    firebaseDataRepository.postMeasureRecord(
                        it.itemDataType.measure.MeasureData?.id!!, MeasureLog(
                            result = 1,
                            createTime = Timestamp.now()
                        )
                    )
                }

                is ItemDataType.CareType -> {
                    firebaseDataRepository.postCareRecord(
                        it.itemDataType.care.CareData?.id!!, CareLog(
                            result = 1,
                            createTime = Timestamp.now()
                        )
                    )
                }

                is ItemDataType.ActivityType -> {
                    firebaseDataRepository.postActivityRecord(
                        it.itemDataType.activity.ActivityData?.id!!, ActivityLog(
                            result = 1,
                            createTime = Timestamp.now()
                        )
                    )
                }

            }
        }
    }

    /*
        DrugLog and ActivityLog will post here.
        CareLog and MeasureLog will post at RecordViewModel.
     */

    private val finishedLogList = mutableListOf<SwipeData>()

    fun getFinishedLog(swipeData: SwipeData) {
        finishedLogList.add(swipeData)
    }

    fun undoSwipeToLog() {
        val lastData = finishedLogList.last()
        finishedLogList.removeLast()

        val currentList = itemDataMediator.value
        currentList?.add(lastData.position, lastData.itemDataType)
        itemDataMediator.value = currentList
    }

    fun postFinishDrugAndActivityLog() {
        finishedLogList.forEach {
            when (it.itemDataType) {
                is ItemDataType.DrugType -> {
                    firebaseDataRepository.postDrugRecord(
                        it.itemDataType.drug.DrugData?.id!!, DrugLog(
                            result = 0,
                            createTime = Timestamp.now()
                        )
                    )
                }

                is ItemDataType.ActivityType -> {
                    firebaseDataRepository.postActivityRecord(
                        it.itemDataType.activity.ActivityData?.id!!, ActivityLog(
                            result = 0,
                            createTime = Timestamp.now()
                        )
                    )
                }
            }
        }
    }
}

sealed class HomePageItem() {

    object AddNewItem : HomePageItem()
    object TodayAbstract : HomePageItem()
    object NextTask : HomePageItem()
}

sealed class ItemDataType() {

    data class TimeType(val time: String, val timeInt: Int) : ItemDataType()
    data class DrugType(val drug: ItemData, val timeInt: Int) : ItemDataType()
    data class MeasureType(val measure: ItemData, val timeInt: Int) : ItemDataType()
    data class ActivityType(val activity: ItemData, val timeInt: Int) : ItemDataType()
    data class CareType(val care: ItemData, val timeInt: Int) : ItemDataType()
}

data class SwipeData(
    val itemDataType: ItemDataType,
    val position: Int
)