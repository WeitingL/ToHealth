package com.weiting.tohealth.homepage

import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.data.*
import com.weiting.tohealth.util.ItemArranger
import com.weiting.tohealth.util.NotificationGenerator
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt
import com.weiting.tohealth.util.Util.isToday
import com.weiting.tohealth.util.Util.toTimeFromTimeStamp
import kotlinx.coroutines.*

class HomeViewModel(private val firebaseDataRepository: FirebaseRepository) :
    ViewModel(),
    NotificationGenerator {

    /*
       Percentage of the completion
     */

    private val _isTheNewbie = MutableLiveData<Boolean>()
    val isTheNewbie: LiveData<Boolean>
        get() = _isTheNewbie

    private val _totalTask = MutableLiveData<Int>()
    val totalTask: LiveData<Int>
        get() = _totalTask

    private val _completedTask = MutableLiveData<Int>()
    val completedTask: LiveData<Int>
        get() = _completedTask

    private val _allCompleted = MutableLiveData<Boolean>()
    val allCompleted: LiveData<Boolean>
        get() = _allCompleted

    val welcomeSlogan = MutableLiveData<Timestamp>().apply {
        value = Timestamp.now()
    }

    fun taskCompleted() {
        _allCompleted.value = totalTask.value == completedTask.value
    }

    init {
        _totalTask.value = 0
        _completedTask.value = 0
        _allCompleted.value = false
        _isTheNewbie.value = true

        viewModelScope.launch {
            if (Firebase.auth.currentUser != null)
                UserManager.UserInfo =
                    firebaseDataRepository.getUser(UserManager.UserInfo.id!!)
        }
    }

    /*
        Logic about todoList after get the livedata from firebase.
        Totally 4 LiveData are needed order by time(hour and min).

        Identify the finished mission by numbers and created time of ItemLog.
     */

    // TODO Refactor 1st
    //LiveData from firebase
    private val drugList =
        firebaseDataRepository.getLiveDrugs(UserManager.UserInfo.id ?: "")
    private val measureList =
        firebaseDataRepository.getLiveMeasures(UserManager.UserInfo.id ?: "")
    private val activityList =
        firebaseDataRepository.getLiveEvents(UserManager.UserInfo.id ?: "")
    private val careList =
        firebaseDataRepository.getLiveCares(UserManager.UserInfo.id ?: "")

    //Isolate the todolist and store separately.
    private val drugCurrentList = mutableListOf<ItemDataType>()
    private val measureCurrentList = mutableListOf<ItemDataType>()
    private val activityCurrentList = mutableListOf<ItemDataType>()
    private val careCurrentList = mutableListOf<ItemDataType>()
    private val timeCurrentList = mutableListOf<ItemDataType>()

    private val timeIntList = mutableListOf<Int>()

    val itemDataMediator = MediatorLiveData<MutableList<ItemDataType>>().apply {
        addSource(drugList) { drugList ->
            viewModelScope.launch {

                drugCurrentList.clear()
                isNewBie(drugList)

                val ongoingDrugs = drugList.filter { it.status == 0 }

                ongoingDrugs.forEach { drug ->
                    val logTimeTags = mutableListOf<Int>()
                    // Get all logs and filter to be created today.
                    drug.drugLogs =
                        firebaseDataRepository.getDrugLogs(drug.id ?: "")
                            .filter { isToday(it.createdTime) }

                    drug.drugLogs.forEach { drugLog ->
                        logTimeTags.add(drugLog.timeTag ?: 0)
                        logTimeTags.distinct()
                        logTimeTags.sort()
                    }

                    if (ItemArranger().isThatDayNeedToDo(
                            ItemData(DrugData = drug),
                            Timestamp.now()
                        )
                    ) {
                        drug.executedTime.forEach {
                            _totalTask.value = _totalTask.value?.plus(1)

                            // Skip the item log that time tag is the same with exist time tag.
                            if (getTimeStampToTimeInt(it) !in logTimeTags) {

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

                            } else {
                                _completedTask.value = _completedTask.value?.plus(1)
                            }
                        }
                    }
                }
                value = reAssignValue()
            }
        }

        addSource(measureList) { measureList ->
            viewModelScope.launch {

                measureCurrentList.clear()
                isNewBie(measureList)

                val ongoingMeasures = measureList.filter { it.status == 0 }


                ongoingMeasures.forEach { measure ->
                    val logTimeTags = mutableListOf<Int>()
                    measure.measureLogs =
                        firebaseDataRepository.getMeasureLogs(measure.id ?: "")
                            .filter { isToday(it.createdTime) }

                    measure.measureLogs.forEach { measureLog ->
                        logTimeTags.add(measureLog.timeTag ?: 0)
                        logTimeTags.distinct()
                        logTimeTags.sort()
                    }

                    if (ItemArranger().isThatDayNeedToDo(
                            ItemData(MeasureData = measure),
                            Timestamp.now()
                        )
                    ) {
                        measure.executedTime.forEach {
                            _totalTask.value = _totalTask.value?.plus(1)
                            if (getTimeStampToTimeInt(it) !in logTimeTags) {
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

                            } else {
                                _completedTask.value = _completedTask.value?.plus(1)
                            }
                        }
                    }
                }
                value = reAssignValue()
            }
        }

        addSource(activityList) { activityList ->
            viewModelScope.launch {
                activityCurrentList.clear()
                isNewBie(activityList)

                val activityListByFilter = activityList.filter { it.status == 0 }



                activityListByFilter.forEach { event ->
                    val logTimeTags = mutableListOf<Int>()
                    event.eventLogs =
                        firebaseDataRepository.getEventLogs(event.id ?: "")
                            .filter { isToday(it.createdTime) }

                    event.eventLogs.forEach { activityLog ->
                        logTimeTags.add(activityLog.timeTag!!)
                        logTimeTags.distinct()
                        logTimeTags.sort()
                    }

                    if (ItemArranger().isThatDayNeedToDo(
                            ItemData(EventData = event),
                            Timestamp.now()
                        )
                    ) {
                        event.executedTime.forEach {
                            _totalTask.value = _totalTask.value?.plus(1)
                            if (getTimeStampToTimeInt(it) !in logTimeTags) {
                                activityCurrentList.add(
                                    ItemDataType.EventType(
                                        ItemData(EventData = event),
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

                            } else {
                                _completedTask.value = _completedTask.value?.plus(1)
                            }
                        }
                    }
                }
                value = reAssignValue()
            }
        }

        addSource(careList) { careList ->
            viewModelScope.launch {
                careCurrentList.clear()
                isNewBie(careList)


                val careListByFilter = careList.filter {
                    it.status == 0
                }


                careListByFilter.forEach { care ->
                    val logTimeTags = mutableListOf<Int>()
                    care.careLogs =
                        firebaseDataRepository.getCareLogs(care.id ?: "")
                            .filter { isToday(it.createdTime) }


                    care.careLogs.forEach { careLog ->
                        logTimeTags.add(careLog.timeTag!!)
                        logTimeTags.distinct()
                        logTimeTags.sort()
                    }

                    if (ItemArranger().isThatDayNeedToDo(
                            ItemData(CareData = care),
                            Timestamp.now()
                        )
                    ) {
                        care.executeTime.forEach {
                            _totalTask.value = _totalTask.value?.plus(1)
                            if (getTimeStampToTimeInt(it) !in logTimeTags) {
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
                            } else {
                                _completedTask.value = _completedTask.value?.plus(1)
                            }
                        }
                    }
                }
                value = reAssignValue()
            }
        }
    }


    private fun reAssignValue(): MutableList<ItemDataType> {
        val list = mutableListOf<ItemDataType>()

        timeIntList.distinct()
        timeIntList.sort()
        timeIntList.forEach { time ->

            list.addAll(timeCurrentList.filter {
                (it as ItemDataType.TimeType).timeInt == time
            })

            list.addAll(drugCurrentList.filter {
                (it as ItemDataType.DrugType).timeInt == time
            })

            list.addAll(measureCurrentList.filter {
                (it as ItemDataType.MeasureType).timeInt == time
            })

            list.addAll(activityCurrentList.filter {
                (it as ItemDataType.EventType).timeInt == time
            })

            list.addAll(careCurrentList.filter {
                (it as ItemDataType.CareType).timeInt == time
            })
        }
        return list
    }

    private fun isNewBie(list: List<Any>) {
        if (list.isEmpty()) {
            _isTheNewbie.value = false
        }
    }

    /*
       Swipe to Skip
     */

    // Collected the skip item data and time title data.
    private val skipList = mutableListOf<SwipeData>()
    private val skipTimeList = mutableListOf<SwipeData>()

    fun swipeToSkip(swipeData: SwipeData) {
        skipList.add(swipeData)
        _completedTask.value = _completedTask.value?.plus(1)
    }

    fun removeTimeHeader(swipeData: SwipeData) {
        skipTimeList.add(swipeData)
    }

    fun undoSwipeToSkip() {
        val lastData = skipList.last()
        val currentList = itemDataMediator.value

        if (skipTimeList.isNotEmpty() && lastData.position == (skipTimeList.last().position + 1)) {
            currentList?.add(skipTimeList.last().position, skipTimeList.last().itemDataType)
            skipTimeList.removeLast()
        }

        skipList.removeLast()
        _completedTask.value = _completedTask.value?.minus(1)

        currentList?.add(lastData.position, lastData.itemDataType)
        itemDataMediator.value = currentList
    }

    fun postSkipLog() {
        viewModelScope.launch {
            skipList.forEach {
                when (it.itemDataType) {
                    is ItemDataType.DrugType -> {
                        firebaseDataRepository.postDrugLog(
                            it.itemDataType.drug.DrugData?.id!!,
                            DrugLog(
                                timeTag = it.itemDataType.timeInt,
                                result = 1,
                                createdTime = Timestamp.now()
                            )
                        )
                    }

                    is ItemDataType.MeasureType -> {
                        firebaseDataRepository.postMeasureLog(
                            it.itemDataType.measure.MeasureData?.id!!,
                            MeasureLog(
                                id = firebaseDataRepository
                                    .getMeasureLogId(it.itemDataType.measure.MeasureData.id!!),
                                timeTag = it.itemDataType.timeInt,
                                result = 1,
                                createdTime = Timestamp.now()
                            )
                        )
                    }

                    is ItemDataType.CareType -> {
                        firebaseDataRepository.postCareLog(
                            it.itemDataType.care.CareData?.id!!,
                            CareLog(
                                timeTag = it.itemDataType.timeInt,
                                result = 1,
                                createdTime = Timestamp.now()
                            )
                        )
                    }

                    is ItemDataType.EventType -> {
                        firebaseDataRepository.postEventLog(
                            it.itemDataType.Event.EventData?.id!!,
                            EventLog(
                                timeTag = it.itemDataType.timeInt,
                                result = 1,
                                createdTime = Timestamp.now()
                            )
                        )
                    }
                }
            }
            skipList.clear()
            skipTimeList.clear()
        }
    }

    /*
        DrugLog and ActivityLog will post here.
        CareLog and MeasureLog will post at RecordViewModel.
     */

    private val finishedLogList = mutableListOf<SwipeData>()
    private val finishedTimeList = mutableListOf<SwipeData>()

    fun getFinishedLog(swipeData: SwipeData) {
        finishedLogList.add(swipeData)
        _completedTask.value = _completedTask.value?.plus(1)
    }

    fun removeTimeHeaderOfFinished(swipeData: SwipeData) {
        finishedTimeList.add(swipeData)
    }

    fun undoSwipeToLog() {
        val lastData = finishedLogList.last()
        val currentList = itemDataMediator.value

        if (finishedTimeList.isNotEmpty() && lastData.position == (finishedTimeList.last().position + 1)) {
            currentList?.add(finishedTimeList.last().position, finishedTimeList.last().itemDataType)
            finishedTimeList.removeLast()
        }

        finishedLogList.removeLast()
        _completedTask.value = _completedTask.value?.minus(1)

        currentList?.add(lastData.position, lastData.itemDataType)
        itemDataMediator.value = currentList
    }

    fun postFinishDrugAndActivityLog() {
        finishedLogList.forEach {
            when (it.itemDataType) {
                is ItemDataType.DrugType -> {

                    if (isDrugExhausted(it.itemDataType.drug.DrugData!!)) {
                        postDrugExhausted(it.itemDataType.drug.DrugData)
                    }

                    firebaseDataRepository.postDrugLog(
                        it.itemDataType.drug.DrugData.id!!,
                        DrugLog(
                            timeTag = it.itemDataType.timeInt,
                            result = 0,
                            createdTime = Timestamp.now()
                        )
                    )

                    val originStock = it.itemDataType.drug.DrugData.stock
                    val updateStock = originStock - it.itemDataType.drug.DrugData.dose

                    firebaseDataRepository.editStock(
                        it.itemDataType.drug.DrugData.id!!,
                        updateStock
                    )
                }

                is ItemDataType.EventType -> {
                    firebaseDataRepository.postEventLog(
                        it.itemDataType.Event.EventData?.id!!,
                        EventLog(
                            timeTag = it.itemDataType.timeInt,
                            result = 0,
                            createdTime = Timestamp.now()
                        )
                    )
                }
            }
        }
        finishedLogList.clear()
        finishedTimeList.clear()
    }

    private fun postDrugExhausted(drug: Drug) {
        firebaseDataRepository.postNotification(
            AlertMessage(
                userId = UserManager.UserInfo.id,
                itemId = drug.id,
                result = 6,
                createdTime = Timestamp.now()
            )
        )
    }
}

sealed class ItemDataType {

    data class TimeType(val time: String, val timeInt: Int) : ItemDataType()
    data class DrugType(val drug: ItemData, val timeInt: Int) : ItemDataType()
    data class MeasureType(val measure: ItemData, val timeInt: Int) : ItemDataType()
    data class EventType(val Event: ItemData, val timeInt: Int) : ItemDataType()
    data class CareType(val care: ItemData, val timeInt: Int) : ItemDataType()
}

data class SwipeData(

    val itemDataType: ItemDataType,
    val position: Int
)
