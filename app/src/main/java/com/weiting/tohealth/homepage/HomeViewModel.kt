package com.weiting.tohealth.homepage

import android.icu.number.IntegerWidth
import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.data.*
import com.weiting.tohealth.util.ItemArranger
import com.weiting.tohealth.util.AlterMessageGenerator
import com.weiting.tohealth.util.Util
import com.weiting.tohealth.util.Util.getTimeStampToDateInt
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt
import com.weiting.tohealth.util.Util.getTimeStampToTimeString
import kotlinx.coroutines.*

class HomeViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel(),
    AlterMessageGenerator {

    /*
      Percentage of the completion
    */

    private val progressCounter = TodoListProgressCounter()
    private val listManager = TodoListManager()

    private val _isTheNewbie = MutableLiveData<Boolean>()
    val isTheNewbie: LiveData<Boolean>
        get() = _isTheNewbie

    private val _totalTask = MutableLiveData<Int>()
    val totalTask: LiveData<Int>
        get() = _totalTask

    private val _completedTask = MutableLiveData<Int>()
    val completedTask: LiveData<Int>
        get() = _completedTask

    private val _isAllCompleted = MutableLiveData<Boolean>()
    val isAllCompleted: LiveData<Boolean>
        get() = _isAllCompleted

    val welcomeSlogan = MutableLiveData<Timestamp>().apply {
        value = Timestamp.now()
    }

    fun isTaskCompleted() {
        _isAllCompleted.value = totalTask.value == completedTask.value
    }

    init {
        _totalTask.value = 0
        _completedTask.value = 0
        _isAllCompleted.value = false
        _isTheNewbie.value = true

        viewModelScope.launch {
            if (Firebase.auth.currentUser != null)
                UserManager.UserInfo = firebaseDataRepository.getUser(UserManager.UserInfo.id ?: "")
        }

    }

    /*
        Logic about todoList after get the livedata from firebase.
        Totally 4 LiveData are needed order by time(hour and min).

        Identify the finished mission by numbers and created time of ItemLog.
     */

    private var liveDrugs =
        firebaseDataRepository.getLiveDrugs(UserManager.UserInfo.id ?: "")
    private var measureList =
        firebaseDataRepository.getLiveMeasures(UserManager.UserInfo.id ?: "")
    private var eventList =
        firebaseDataRepository.getLiveEvents(UserManager.UserInfo.id ?: "")
    private var careList =
        firebaseDataRepository.getLiveCares(UserManager.UserInfo.id ?: "")


    val itemDataMediator = MediatorLiveData<MutableList<ItemDataType>>().apply {
        addSource(liveDrugs) { drugList ->
            viewModelScope.launch {
                listManager.apply {
                    clearCurrentList(ItemType.DRUG)
                    clearAllItemList(ItemType.DRUG)
                }

                _completedTask.value =
                    _completedTask.value?.minus(progressCounter.getCounterRecord(ItemType.DRUG))

                if (drugList.isNotEmpty()) {
                    isNotNewBie()
                }

                val todayDrugs = drugList.filter {
                    it.status == 0 &&
                            ItemArranger.isThatDayNeedToDo(
                                ItemData(drugData = it), Timestamp.now()
                            )
                }

                todayDrugs.forEach { drug ->

                    //Get all logs today.
                    drug.drugLogs =
                        firebaseDataRepository.getDrugLogs(drug.id ?: "").filter {
                            Util.isToday(it.createdTime)
                        }

                    val logTimeTags = mutableListOf<Int>()
                    drug.drugLogs.forEach { drugLog ->
                        logTimeTags.add(drugLog.timeTag ?: 0)
                    }

                    drug.executedTime.forEach {
                        listManager.getAllItem(ItemData(drugData = drug))

                        //Skip the item log that time tag is the same with exist time tag.
                        if (getTimeStampToTimeInt(it) !in logTimeTags) {
                            listManager.getCurrentItemDataType(
                                ItemDataType.DrugType(
                                    ItemData(drugData = drug),
                                    getTimeStampToTimeInt(it)
                                )
                            )

                            if (getTimeStampToTimeInt(it) !in listManager.timeTitleList) {
                                listManager.apply {
                                    getTimeTitle(getTimeStampToTimeInt(it))
                                    getCurrentItemDataType(
                                        ItemDataType.TimeType(
                                            getTimeStampToTimeString(it),
                                            getTimeStampToTimeInt(it)
                                        )
                                    )
                                }
                            }
                        } else {
                            _completedTask.value = _completedTask.value?.plus(1)
                        }
                    }
                }

                value = listManager.arrangeTodoList()
            }
        }

        addSource(measureList) { measureList ->
            viewModelScope.launch {
                listManager.apply {
                    clearCurrentList(ItemType.MEASURE)
                    clearAllItemList(ItemType.MEASURE)
                }

                _completedTask.value =
                    _completedTask.value?.minus(progressCounter.getCounterRecord(ItemType.MEASURE))

                if (measureList.isNotEmpty()) {
                    isNotNewBie()
                }

                val todayMeasures = measureList.filter {
                    it.status == 0 && ItemArranger.isThatDayNeedToDo(
                        ItemData(measureData = it),
                        Timestamp.now()
                    )
                }

                todayMeasures.forEach { measure ->

                    measure.measureLogs =
                        firebaseDataRepository.getMeasureLogs(measure.id!!)
                            .filter {
                                getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(
                                    Timestamp.now()
                                )
                            }

                    val logTimeTags = mutableListOf<Int>()
                    measure.measureLogs.forEach { measureLog ->
                        logTimeTags.add(measureLog.timeTag!!)
                    }

                    measure.executedTime.forEach {
                        listManager.getAllItem(ItemData(measureData = measure))

                        if (getTimeStampToTimeInt(it) !in logTimeTags) {
                            listManager.getCurrentItemDataType(
                                ItemDataType.MeasureType(
                                    ItemData(measureData = measure),
                                    getTimeStampToTimeInt(it)
                                )
                            )

                            if (getTimeStampToTimeInt(it) !in listManager.timeTitleList) {
                                listManager.apply {
                                    getTimeTitle(getTimeStampToTimeInt(it))
                                    getCurrentItemDataType(
                                        ItemDataType.TimeType(
                                            getTimeStampToTimeString(it),
                                            getTimeStampToTimeInt(it)
                                        )
                                    )
                                }
                            }
                        } else {
                            _completedTask.value = _completedTask.value?.plus(1)
                        }

                    }
                }
                value = listManager.arrangeTodoList()
            }
        }

        addSource(eventList) { eventList ->
            viewModelScope.launch {
                listManager.apply {
                    clearCurrentList(ItemType.EVENT)
                    clearAllItemList(ItemType.EVENT)
                }

                _completedTask.value =
                    _completedTask.value?.minus(progressCounter.getCounterRecord(ItemType.EVENT))

                if (eventList.isNotEmpty()) {
                    isNotNewBie()
                }

                val todayEvents = eventList.filter {
                    it.status == 0 && ItemArranger.isThatDayNeedToDo(
                        ItemData(eventData = it),
                        Timestamp.now()
                    )
                }

                todayEvents.forEach { event ->

                    event.eventLogs =
                        firebaseDataRepository.getEventLogs(event.id!!)
                            .filter {
                                getTimeStampToDateInt(it.createdTime!!) ==
                                        getTimeStampToDateInt(Timestamp.now())
                            }

                    val logTimeTags = mutableListOf<Int>()
                    event.eventLogs.forEach { activityLog ->
                        logTimeTags.add(activityLog.timeTag!!)
                    }

                    event.executedTime.forEach {
                        listManager.getAllItem(ItemData(eventData = event))

                        if (getTimeStampToTimeInt(it) !in logTimeTags) {
                            listManager.getCurrentItemDataType(
                                ItemDataType.EventType(
                                    ItemData(eventData = event),
                                    getTimeStampToTimeInt(it)
                                )
                            )

                            if (getTimeStampToTimeInt(it) !in listManager.timeTitleList) {
                                listManager.apply {
                                    getTimeTitle(getTimeStampToTimeInt(it))
                                    getCurrentItemDataType(
                                        ItemDataType.TimeType(
                                            getTimeStampToTimeString(it),
                                            getTimeStampToTimeInt(it)
                                        )
                                    )
                                }
                            }
                        } else {
                            _completedTask.value = _completedTask.value?.plus(1)
                        }
                    }
                }
                value = listManager.arrangeTodoList()
            }
        }

        addSource(careList) { careList ->
            viewModelScope.launch {
                listManager.apply {
                    clearCurrentList(ItemType.CARE)
                    clearAllItemList(ItemType.CARE)
                }

                _completedTask.value =
                    _completedTask.value?.minus(progressCounter.getCounterRecord(ItemType.CARE))

                if (careList.isNotEmpty()) {
                    isNotNewBie()
                }

                val todayCares = careList.filter {
                    it.status == 0 && ItemArranger.isThatDayNeedToDo(
                        ItemData(careData = it),
                        Timestamp.now()
                    )
                }

                todayCares.forEach { care ->

                    care.careLogs =
                        firebaseDataRepository.getCareLogs(care.id!!).filter {
                            getTimeStampToDateInt(it.createdTime!!) ==
                                    getTimeStampToDateInt(Timestamp.now())
                        }

                    val logTimeTags = mutableListOf<Int>()
                    care.careLogs.forEach { careLog ->
                        logTimeTags.add(careLog.timeTag?:0)
                    }


                    care.executedTime.forEach {
                        listManager.getAllItem(ItemData(careData = care))

                        if (getTimeStampToTimeInt(it) !in logTimeTags) {
                            listManager.getCurrentItemDataType(
                                ItemDataType.CareType(
                                    ItemData(careData = care),
                                    getTimeStampToTimeInt(it)
                                )
                            )

                            if (getTimeStampToTimeInt(it) !in listManager.timeTitleList) {
                                listManager.apply {
                                    getTimeTitle(getTimeStampToTimeInt(it))
                                    getCurrentItemDataType(
                                        ItemDataType.TimeType(
                                            getTimeStampToTimeString(it),
                                            getTimeStampToTimeInt(it)
                                        )
                                    )
                                }
                            }
                        } else {
                            _completedTask.value = _completedTask.value?.plus(1)
                        }
                    }
                }
                value = listManager.arrangeTodoList()
            }
        }
    }

    private fun isNotNewBie() {
        _isTheNewbie.value = false
    }

    fun getAllTaskNumber() {
        _totalTask.value = listManager.getAllTodoListNum()
    }

    /*
       Swipe to Skip
     */

    // Collected the skip item data and time title data.
    private val skipList = mutableListOf<SwipeData>()
    private val skipTimeList = mutableListOf<SwipeData>()

    fun swipeToSkip(swipeData: SwipeData) {
        _completedTask.value = _completedTask.value?.plus(1)
        skipList.add(swipeData)
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
                progressCounter.postLog(it)
                when (it.itemDataType) {
                    is ItemDataType.DrugType -> {
                        firebaseDataRepository.postDrugLog(
                            it.itemDataType.drug.drugData?.id!!, DrugLog(
                                timeTag = it.itemDataType.timeInt,
                                result = 1,
                                createdTime = Timestamp.now()
                            )
                        )
                        liveDrugs =
                            firebaseDataRepository.getLiveDrugs(UserManager.UserInfo.id ?: "")
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
                        measureList =
                            firebaseDataRepository.getLiveMeasures(UserManager.UserInfo.id ?: "")
                    }

                    is ItemDataType.CareType -> {
                        firebaseDataRepository.postCareLog(
                            it.itemDataType.care.careData?.id!!, CareLog(
                                timeTag = it.itemDataType.timeInt,
                                result = 1,
                                createdTime = Timestamp.now()
                            )
                        )
                        careList =
                            firebaseDataRepository.getLiveCares(UserManager.UserInfo.id ?: "")
                    }

                    is ItemDataType.EventType -> {
                        firebaseDataRepository.postEventLog(
                            it.itemDataType.event.eventData?.id!!, EventLog(
                                timeTag = it.itemDataType.timeInt,
                                result = 1,
                                createdTime = Timestamp.now()
                            )
                        )
                        eventList =
                            firebaseDataRepository.getLiveEvents(UserManager.UserInfo.id ?: "")
                    }
                }
            }
            cleanSkipList()
        }
    }

    private fun cleanSkipList() {
        skipList.clear()
        skipTimeList.forEach {
            listManager.removeTimeTitle((it.itemDataType as ItemDataType.TimeType).timeInt)
        }
        skipTimeList.clear()
    }

    /*
        DrugLog and ActivityLog will post here.
        CareLog and MeasureLog will post at RecordViewModel.
     */

    private val finishedLogList = mutableListOf<SwipeData>()
    private val finishedTimeList = mutableListOf<SwipeData>()

    fun swipeToFinished(swipeData: SwipeData) {
        _completedTask.value = _completedTask.value?.plus(1)
        finishedLogList.add(swipeData)
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
            progressCounter.postLog(it)
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
                    eventList =
                        firebaseDataRepository.getLiveEvents(UserManager.UserInfo.id ?: "")
                }
            }
        }
        clearPostLogList()
    }

    private fun clearPostLogList() {
        finishedLogList.clear()
        finishedTimeList.forEach {
            listManager.removeTimeTitle((it.itemDataType as ItemDataType.TimeType).timeInt)
        }
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