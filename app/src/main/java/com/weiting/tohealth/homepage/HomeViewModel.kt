package com.weiting.tohealth.homepage

import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.data.*
import com.weiting.tohealth.util.ItemArranger
import com.weiting.tohealth.util.NotificationGenerator
import com.weiting.tohealth.util.Util.getTimeStampToDateInt
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt
import com.weiting.tohealth.util.Util.getTimeStampToTimeString
import kotlinx.coroutines.*

class HomeViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel(),
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
                UserManager.UserInfo = firebaseDataRepository.getUser(UserManager.UserInfo.id!!)
        }

    }

    /*
        Logic about todoList after get the livedata from firebase.
        Totally 4 LiveData are needed order by time(hour and min).

        Identify the finished mission by numbers and created time of ItemLog.
     */

    private val drugList =
        firebaseDataRepository.getLiveDrugs(UserManager.UserInfo.id ?: "")
    private val measureList =
        firebaseDataRepository.getLiveMeasures(UserManager.UserInfo.id ?: "")
    private val activityList =
        firebaseDataRepository.getLiveEvents(UserManager.UserInfo.id ?: "")
    private val careList =
        firebaseDataRepository.getLiveCares(UserManager.UserInfo.id ?: "")

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
                if (drugList.isNotEmpty()) {
                    isNotNewBie()
                }

                val logTimeTags = mutableListOf<Int>()
                val drugListByFilter = drugList.filter {
                    it.status == 0 &&
                            ItemArranger.isThatDayNeedToDo(
                                ItemData(drugData = it), Timestamp.now()
                            )
                }

                drugListByFilter.forEach { drug ->

                    //Get all logs today.
                    drug.drugLogs =
                        firebaseDataRepository.getDrugLogs(drug.id!!).filter {
                            getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(
                                Timestamp.now()
                            )
                        }

                    drug.drugLogs.forEach { drugLog ->
                        logTimeTags.add(drugLog.timeTag!!)
                        logTimeTags.sort()
                    }

                    drug.executedTime.forEach {
                        _totalTask.value = _totalTask.value?.plus(1)

                        //Skip the item log that time tag is the same with exist time tag.
                        if (getTimeStampToTimeInt(it) !in logTimeTags) {
                            drugCurrentList.add(
                                ItemDataType.DrugType(
                                    ItemData(drugData = drug),
                                    getTimeStampToTimeInt(it)
                                )
                            )

                            if (getTimeStampToTimeInt(it) !in timeIntList) {
                                timeIntList.add(getTimeStampToTimeInt(it))
                                timeCurrentList.add(
                                    ItemDataType.TimeType(
                                        getTimeStampToTimeString(it),
                                        getTimeStampToTimeInt(it)
                                    )
                                )
                            }
                        } else {
                            _completedTask.value = _completedTask.value?.plus(1)
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

        addSource(measureList) { measureList ->
            viewModelScope.launch {

                measureCurrentList.clear()
                if (measureList.isNotEmpty()) {
                    isNotNewBie()
                }

                val measureListByFilter = measureList.filter {
                    it.status == 0
                }

                measureListByFilter.forEach { measure ->

                    measure.measureLogs =
                        firebaseDataRepository.getMeasureLogs(measure.id!!)
                            .filter {
                                getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(
                                    Timestamp.now()
                                )
                            }

                    val todayLogCreateTimeIntList = mutableListOf<Int>()
                    measure.measureLogs.forEach { measureLog ->
                        todayLogCreateTimeIntList.add(measureLog.timeTag!!)
                        todayLogCreateTimeIntList.sort()
                    }

                    if (ItemArranger.isThatDayNeedToDo(
                            ItemData(measureData = measure),
                            Timestamp.now()
                        )
                    ) {
                        measure.executedTime.forEach {
                            _totalTask.value = _totalTask.value?.plus(1)
                            if (getTimeStampToTimeInt(it) !in todayLogCreateTimeIntList) {
                                measureCurrentList.add(
                                    ItemDataType.MeasureType(
                                        ItemData(measureData = measure),
                                        getTimeStampToTimeInt(it)
                                    )
                                )

                                if (getTimeStampToTimeInt(it) !in timeIntList) {
                                    timeIntList.add(getTimeStampToTimeInt(it))
                                    timeCurrentList.add(
                                        ItemDataType.TimeType(
                                            getTimeStampToTimeString(it),
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

        addSource(activityList) { activityList ->
            viewModelScope.launch {
                activityCurrentList.clear()

                if (activityList.isNotEmpty()) {
                    isNotNewBie()
                }

                val activityListByFilter = activityList.filter {
                    it.status == 0
                }

                activityListByFilter.forEach { activity ->

                    activity.eventLogs =
                        firebaseDataRepository.getEventLogs(activity.id!!)
                            .filter {
                                getTimeStampToDateInt(it.createdTime!!) ==
                                        getTimeStampToDateInt(Timestamp.now())
                            }

                    val todayLogCreateTimeIntList = mutableListOf<Int>()
                    activity.eventLogs.forEach { activityLog ->
                        todayLogCreateTimeIntList.add(activityLog.timeTag!!)
                        todayLogCreateTimeIntList.sort()
                    }

                    if (ItemArranger.isThatDayNeedToDo(
                            ItemData(eventData = activity),
                            Timestamp.now()
                        )
                    ) {
                        activity.executedTime.forEach {
                            _totalTask.value = _totalTask.value?.plus(1)
                            if (getTimeStampToTimeInt(it) !in todayLogCreateTimeIntList) {
                                activityCurrentList.add(
                                    ItemDataType.EventType(
                                        ItemData(eventData = activity),
                                        getTimeStampToTimeInt(it)
                                    )
                                )

                                if (getTimeStampToTimeInt(it) !in timeIntList) {
                                    timeIntList.add(getTimeStampToTimeInt(it))
                                    timeCurrentList.add(
                                        ItemDataType.TimeType(
                                            getTimeStampToTimeString(it),
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

        addSource(careList) { careList ->
            viewModelScope.launch {
                careCurrentList.clear()

                if (careList.isNotEmpty()) {
                    isNotNewBie()
                }

                val careListByFilter = careList.filter {
                    it.status == 0
                }

                careListByFilter.forEach { care ->

                    care.careLogs =
                        firebaseDataRepository.getCareLogs(care.id!!).filter {
                            getTimeStampToDateInt(it.createdTime!!) ==
                                    getTimeStampToDateInt(Timestamp.now())
                        }

                    val todayLogCreateTimeIntList = mutableListOf<Int>()
                    care.careLogs.forEach { careLog ->
                        todayLogCreateTimeIntList.add(careLog.timeTag!!)
                        todayLogCreateTimeIntList.sort()
                    }

                    if (ItemArranger.isThatDayNeedToDo(
                            ItemData(careData = care),
                            Timestamp.now()
                        )
                    ) {
                        care.executedTime.forEach {
                            _totalTask.value = _totalTask.value?.plus(1)
                            if (getTimeStampToTimeInt(it) !in todayLogCreateTimeIntList) {
                                careCurrentList.add(
                                    ItemDataType.CareType(
                                        ItemData(careData = care),
                                        getTimeStampToTimeInt(it)
                                    )
                                )

                                if (getTimeStampToTimeInt(it) !in timeIntList) {
                                    timeIntList.add(getTimeStampToTimeInt(it))
                                    timeCurrentList.add(
                                        ItemDataType.TimeType(
                                            getTimeStampToTimeString(it),
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
                if ((it as ItemDataType.EventType).timeInt == time) {
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

    private fun isNotNewBie() {
        _isTheNewbie.value = false
    }

    /*
       Swipe to Skip
     */

    // Collected the skip item data and time title data.
    private val skipList = mutableListOf<SwipeData>()
    private val skipTimeList = mutableListOf<SwipeData>()

    fun swipeToSkip(swipeData: SwipeData) {
//        if (skipList.isNotEmpty()){
//            postSkipLog()
//            skipList.clear()
//        }
        skipList.add(swipeData)
        _completedTask.value = _completedTask.value?.plus(1)
    }

    fun removeTimeHeader(swipeData: SwipeData) {
//        if (skipTimeList.isNotEmpty()){
//            skipTimeList.clear()
//        }
        skipTimeList.add(swipeData)
    }

    fun undoSwipeToSkip() {
        val lastData = skipList.last()
        val currentList = itemDataMediator.value

        if (skipTimeList.isNotEmpty() && lastData.position == (skipTimeList.last().position + 1)) {
//            Log.i("data", skipTimeList.last().itemDataType.toString())
            currentList?.add(skipTimeList.last().position, skipTimeList.last().itemDataType)
            skipTimeList.removeLast()
        }

        skipList.removeLast()
        _completedTask.value = _completedTask.value?.minus(1)

        currentList?.add(lastData.position, lastData.itemDataType)
//        Log.i("data", "${lastData.position}: ${currentList}")
        itemDataMediator.value = currentList
    }

    fun postSkipLog() {
        viewModelScope.launch {
            skipList.forEach {
                when (it.itemDataType) {
                    is ItemDataType.DrugType -> {
                        firebaseDataRepository.postDrugLog(
                            it.itemDataType.drug.drugData?.id!!, DrugLog(
                                timeTag = it.itemDataType.timeInt,
                                result = 1,
                                createdTime = Timestamp.now()
                            )
                        )
                    }

                    is ItemDataType.MeasureType -> {
                        firebaseDataRepository.postMeasureLog(
                            it.itemDataType.measure.measureData?.id!!, MeasureLog(
                                id = firebaseDataRepository
                                    .getMeasureLogId(it.itemDataType.measure.measureData.id!!),
                                timeTag = it.itemDataType.timeInt,
                                result = 1,
                                createdTime = Timestamp.now()
                            )
                        )
                    }

                    is ItemDataType.CareType -> {
                        firebaseDataRepository.postCareLog(
                            it.itemDataType.care.careData?.id!!, CareLog(
                                timeTag = it.itemDataType.timeInt,
                                result = 1,
                                createdTime = Timestamp.now()
                            )
                        )
                    }

                    is ItemDataType.EventType -> {
                        firebaseDataRepository.postEventLog(
                            it.itemDataType.event.eventData?.id!!, EventLog(
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

                    if (isDrugExhausted(it.itemDataType.drug.drugData ?: Drug())) {
                        postDrugExhausted(it.itemDataType.drug.drugData ?: Drug())
                    }

                    firebaseDataRepository.postDrugLog(
                        it.itemDataType.drug.drugData?.id ?: "", DrugLog(
                            timeTag = it.itemDataType.timeInt,
                            result = 0,
                            createdTime = Timestamp.now()
                        )
                    )

                    val originStock = it.itemDataType.drug.drugData?.stock ?: 0F
                    val dose = it.itemDataType.drug.drugData?.dose ?: 0F
                    val updateStock = originStock - dose

                    firebaseDataRepository.editStock(
                        it.itemDataType.drug.drugData?.id ?: "",
                        updateStock
                    )
                }

                is ItemDataType.EventType -> {
                    firebaseDataRepository.postEventLog(
                        it.itemDataType.event.eventData?.id!!, EventLog(
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
        firebaseDataRepository.postAlertMessage(
            AlertMessage(
                userId = UserManager.UserInfo.id,
                itemId = drug.id,
                result = 6,
                createdTime = Timestamp.now()
            )
        )
    }
}

sealed class ItemDataType() {

    data class TimeType(val time: String, val timeInt: Int) : ItemDataType()
    data class DrugType(val drug: ItemData, val timeInt: Int) : ItemDataType()
    data class MeasureType(val measure: ItemData, val timeInt: Int) : ItemDataType()
    data class EventType(val event: ItemData, val timeInt: Int) : ItemDataType()
    data class CareType(val care: ItemData, val timeInt: Int) : ItemDataType()
}

data class SwipeData(

    val itemDataType: ItemDataType,
    val position: Int
)