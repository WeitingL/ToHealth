package com.weiting.tohealth.homepage

import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.data.*
import com.weiting.tohealth.util.ItemArranger
import com.weiting.tohealth.util.AlterMessageGenerator
import com.weiting.tohealth.util.Util
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt
import com.weiting.tohealth.util.Util.getTimeStampToTimeString
import kotlinx.coroutines.*

class HomeViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel(),
    AlterMessageGenerator {

    /*
      Percentage of the completion
    */

    private val progressCounter = TodoListLogCounter()
    private val todoListManager = TodoListManager()

    private val _isTheNewbie = MutableLiveData<Boolean>()
    val isTheNewbie: LiveData<Boolean>
        get() = _isTheNewbie

    private val _totalTask = MutableLiveData<Int>()
    val totalTask: LiveData<Int>
        get() = _totalTask

    private val _completedTask = MutableLiveData<Int>()
    val completedTask: LiveData<Int>
        get() = _completedTask

    val welcomeSlogan = MutableLiveData<Timestamp>().apply {
        value = Timestamp.now()
    }

    init {
        _totalTask.value = 0
        _completedTask.value = 0
        _isTheNewbie.value = true

        viewModelScope.launch {
            if (Firebase.auth.currentUser != null)
                UserManager.UserInfo = when (val result =
                    firebaseDataRepository.getUser(UserManager.UserInfo.id ?: "")) {
                    is Result.Success -> result.data
                    else -> User()
                }
        }
    }

    val isAllCompleted = MediatorLiveData<Boolean>().apply {
        addSource(totalTask) {
            value = when (totalTask == completedTask) {
                true -> true
                false -> false
            }
        }
        addSource(completedTask) {
            value = when (totalTask == completedTask) {
                true -> true
                false -> false
            }
        }
    }

    /*
        TodoList after getting the livedata from firebase.
        Totally 4 LiveData are needed order by time(hour and min).

        Identify the finished mission by timeTag and created time of ItemLog.
     */

    private var liveDrugs =
        firebaseDataRepository.getLiveDrugs(UserManager.UserInfo.id ?: "")
    private var liveMeasures =
        firebaseDataRepository.getLiveMeasures(UserManager.UserInfo.id ?: "")
    private var liveEvents =
        firebaseDataRepository.getLiveEvents(UserManager.UserInfo.id ?: "")
    private var liveCares =
        firebaseDataRepository.getLiveCares(UserManager.UserInfo.id ?: "")


    val itemDataMediator = MediatorLiveData<MutableList<ItemDataType>>().apply {
        addSource(liveDrugs) { drugList ->
            viewModelScope.launch {
                todoListManager.apply {
                    clearCurrentList(ItemType.DRUG)
                    clearAllMissionList(ItemType.DRUG)
                }

                progressCounter.clearCount(ItemType.DRUG)

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
                    val data =
                        when (val result = firebaseDataRepository.getDrugLogs(drug.id ?: "")) {
                            is Result.Success -> result.data
                            else -> listOf()
                        }

                    drug.drugLogs = data.filter {
                        Util.isToday(it.createdTime)
                    }

                    val logTimeTags = mutableListOf<Int>()
                    drug.drugLogs.forEach { drugLog ->
                        logTimeTags.add(drugLog.timeTag ?: 0)
                    }

                    drug.executedTime.forEach {
                        todoListManager.addAllMission(ItemData(drugData = drug))

                        //Skip the item log that time tag is the same with exist time tag.
                        if (getTimeStampToTimeInt(it) !in logTimeTags) {
                            todoListManager.addCurrentItemDataType(
                                ItemDataType.DrugType(
                                    ItemData(drugData = drug),
                                    getTimeStampToTimeInt(it)
                                )
                            )

                            if (getTimeStampToTimeInt(it) !in todoListManager.timeTitleList) {
                                todoListManager.apply {
                                    addTimeTitle(getTimeStampToTimeInt(it))
                                    addCurrentItemDataType(
                                        ItemDataType.TimeType(
                                            getTimeStampToTimeString(it),
                                            getTimeStampToTimeInt(it)
                                        )
                                    )
                                }
                            }
                        } else {
                            progressCounter.addPostLogCount(ItemType.DRUG)
                        }
                    }
                }
                value = todoListManager.arrangeTodoList()
            }
        }

        addSource(liveMeasures) { measureList ->
            viewModelScope.launch {
                todoListManager.apply {
                    clearCurrentList(ItemType.MEASURE)
                    clearAllMissionList(ItemType.MEASURE)
                }

                progressCounter.clearCount(ItemType.MEASURE)

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

                    val data = when (val result =
                        firebaseDataRepository.getMeasureLogs(measure.id ?: "")) {
                        is Result.Success -> result.data
                        else -> listOf()
                    }

                    measure.measureLogs = data.filter {
                        Util.isToday(it.createdTime)
                    }

                    val logTimeTags = mutableListOf<Int>()
                    measure.measureLogs.forEach { measureLog ->
                        logTimeTags.add(measureLog.timeTag!!)
                    }

                    measure.executedTime.forEach {
                        todoListManager.addAllMission(ItemData(measureData = measure))

                        if (getTimeStampToTimeInt(it) !in logTimeTags) {
                            todoListManager.addCurrentItemDataType(
                                ItemDataType.MeasureType(
                                    ItemData(measureData = measure),
                                    getTimeStampToTimeInt(it)
                                )
                            )

                            if (getTimeStampToTimeInt(it) !in todoListManager.timeTitleList) {
                                todoListManager.apply {
                                    addTimeTitle(getTimeStampToTimeInt(it))
                                    addCurrentItemDataType(
                                        ItemDataType.TimeType(
                                            getTimeStampToTimeString(it),
                                            getTimeStampToTimeInt(it)
                                        )
                                    )
                                }
                            }
                        } else {
                            progressCounter.addPostLogCount(ItemType.MEASURE)
                        }

                    }
                }
                value = todoListManager.arrangeTodoList()
            }
        }

        addSource(liveEvents) { eventList ->
            viewModelScope.launch {
                todoListManager.apply {
                    clearCurrentList(ItemType.EVENT)
                    clearAllMissionList(ItemType.EVENT)
                }

                progressCounter.clearCount(ItemType.EVENT)

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

                    val data =
                        when (val result = firebaseDataRepository.getEventLogs(event.id ?: "")) {
                            is Result.Success -> result.data
                            else -> listOf()
                        }

                    event.eventLogs = data.filter {
                        Util.isToday(it.createdTime)
                    }

                    val logTimeTags = mutableListOf<Int>()
                    event.eventLogs.forEach { eventLog ->
                        logTimeTags.add(eventLog.timeTag!!)
                    }

                    event.executedTime.forEach {
                        todoListManager.addAllMission(ItemData(eventData = event))

                        if (getTimeStampToTimeInt(it) !in logTimeTags) {
                            todoListManager.addCurrentItemDataType(
                                ItemDataType.EventType(
                                    ItemData(eventData = event),
                                    getTimeStampToTimeInt(it)
                                )
                            )

                            if (getTimeStampToTimeInt(it) !in todoListManager.timeTitleList) {
                                todoListManager.apply {
                                    addTimeTitle(getTimeStampToTimeInt(it))
                                    addCurrentItemDataType(
                                        ItemDataType.TimeType(
                                            getTimeStampToTimeString(it),
                                            getTimeStampToTimeInt(it)
                                        )
                                    )
                                }
                            }
                        } else {
                            progressCounter.addPostLogCount(ItemType.EVENT)
                        }
                    }
                }
                value = todoListManager.arrangeTodoList()
            }
        }

        addSource(liveCares) { careList ->
            viewModelScope.launch {
                todoListManager.apply {
                    clearCurrentList(ItemType.CARE)
                    clearAllMissionList(ItemType.CARE)
                }

                progressCounter.clearCount(ItemType.CARE)

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

                    val data =
                        when (val result = firebaseDataRepository.getCareLogs(care.id ?: "")) {
                            is Result.Success -> result.data
                            else -> listOf()
                        }

                    care.careLogs = data.filter {
                        Util.isToday(it.createdTime)
                    }

                    val logTimeTags = mutableListOf<Int>()
                    care.careLogs.forEach { careLog ->
                        logTimeTags.add(careLog.timeTag ?: 0)
                    }


                    care.executedTime.forEach {
                        todoListManager.addAllMission(ItemData(careData = care))

                        if (getTimeStampToTimeInt(it) !in logTimeTags) {
                            todoListManager.addCurrentItemDataType(
                                ItemDataType.CareType(
                                    ItemData(careData = care),
                                    getTimeStampToTimeInt(it)
                                )
                            )

                            if (getTimeStampToTimeInt(it) !in todoListManager.timeTitleList) {
                                todoListManager.apply {
                                    addTimeTitle(getTimeStampToTimeInt(it))
                                    addCurrentItemDataType(
                                        ItemDataType.TimeType(
                                            getTimeStampToTimeString(it),
                                            getTimeStampToTimeInt(it)
                                        )
                                    )
                                }
                            }
                        } else {
                            progressCounter.addPostLogCount(ItemType.CARE)
                        }
                    }
                }
                value = todoListManager.arrangeTodoList()
            }
        }
    }

    private fun isNotNewBie() {
        _isTheNewbie.value = false
    }

    fun getAllTaskNumber() {
        _totalTask.value = todoListManager.getAllTodoListNum()
    }

    fun getAllCheckedTaskCount() {
        _completedTask.value = progressCounter.getTotalCheckedLogNum()
    }

    /*
       Swipe to Skip
     */

    // Collected the skip item data and time title data.
    private val swipeSkipListManager = SwipeSkipListManager()

    fun swipeToSkip(swipeData: SwipeData) {
        progressCounter.addPostLogCount(swipeData)
        getAllCheckedTaskCount()
        swipeSkipListManager.getSwipeToSkip(swipeData)
    }

    fun removeTimeHeader(swipeData: SwipeData) {
        swipeSkipListManager.getSwipeToSkip(swipeData)
    }

    fun undoSwipeToSkip() {
        val currentList = itemDataMediator.value
        progressCounter.decreasePostLogCount()
        itemDataMediator.value =
            swipeSkipListManager.reBuildCurrentList(currentList ?: mutableListOf())
    }

    fun postSkipLog() {
        viewModelScope.launch {
            val skipData = swipeSkipListManager.skipList.first()
            when (skipData.itemDataType) {
                is ItemDataType.DrugType -> {
                    firebaseDataRepository.postDrugLog(
                        skipData.itemDataType.drug.drugData?.id!!, DrugLog(
                            timeTag = skipData.itemDataType.timeInt,
                            result = 1,
                            createdTime = Timestamp.now()
                        )
                    )
                }

                is ItemDataType.MeasureType -> {
                    val id = when (val result = firebaseDataRepository
                        .getMeasureLogId(skipData.itemDataType.measure.measureData?.id ?: "")) {
                        is Result.Success -> result.data
                        else -> null
                    }

                    firebaseDataRepository.postMeasureLog(
                        skipData.itemDataType.measure.measureData?.id ?: "",
                        MeasureLog(
                            id = id,
                            timeTag = skipData.itemDataType.timeInt,
                            result = 1,
                            createdTime = Timestamp.now()
                        )
                    )
                }

                is ItemDataType.CareType -> {
                    firebaseDataRepository.postCareLog(
                        skipData.itemDataType.care.careData?.id!!, CareLog(
                            timeTag = skipData.itemDataType.timeInt,
                            result = 1,
                            createdTime = Timestamp.now()
                        )
                    )
                }

                is ItemDataType.EventType -> {
                    firebaseDataRepository.postEventLog(
                        skipData.itemDataType.event.eventData?.id!!, EventLog(
                            timeTag = skipData.itemDataType.timeInt,
                            result = 1,
                            createdTime = Timestamp.now()
                        )
                    )
                }
            }
            cleanSkipList()
        }
    }

    private fun cleanSkipList() {
        swipeSkipListManager.apply {
            skipList.clear()
            skipTimeList.forEach {
                todoListManager.removeTimeTitle((it.itemDataType as ItemDataType.TimeType).timeInt)
            }
            skipTimeList.clear()
        }
    }

    /*
        DrugLog and ActivityLog will post here.
        CareLog and MeasureLog will post at RecordViewModel.
     */

    private val swipeCheckedListManager = SwipeCheckedListManager()

    fun swipeToFinished(swipeData: SwipeData) {

        if (swipeData.itemDataType is ItemDataType.DrugType) {
            swipeCheckedListManager.addDrugSwipeData(swipeData)
            postFinishDrugAndActivityLog(ItemType.DRUG)
        } else {
            progressCounter.addPostLogCount(swipeData)
            getAllCheckedTaskCount()
            swipeCheckedListManager.addEventSwipeData(swipeData)
        }

    }

    fun removeTimeHeaderOfFinished(swipeData: SwipeData) {
        swipeCheckedListManager.addEventTimeHeader(swipeData)
    }

    fun undoSwipeToLog(itemType: ItemType) {
        when (itemType) {
            ItemType.DRUG -> {
                val logId = swipeCheckedListManager.getDrugLogIdForUndo()
                val drugData = swipeCheckedListManager
                    .getDrugSwipeDataForUndo().itemDataType as ItemDataType.DrugType
                val drugId = drugData.drug.drugData?.id ?: ""
                val drugStock = drugData.drug.drugData?.stock ?: 0F

                firebaseDataRepository.deleteDrugLog(drugId, logId)
                firebaseDataRepository.editStock(drugId, drugStock)

            }
            ItemType.EVENT -> {
                val currentList = itemDataMediator.value
                progressCounter.decreasePostLogCount()
                itemDataMediator.value =
                    swipeCheckedListManager
                        .reBuildCurrentList(currentList ?: mutableListOf())

            }
            else -> {
            }
        }
    }

    fun postFinishDrugAndActivityLog(itemType: ItemType) {
        viewModelScope.launch {
            when (itemType) {
                ItemType.DRUG -> {
                    val drug =
                        swipeCheckedListManager.getDrugSwipeDataForPost()
                            .itemDataType as ItemDataType.DrugType

                    if (isDrugExhausted(drug.drug.drugData ?: Drug())) {
                        postDrugExhausted(drug.drug.drugData ?: Drug())
                    }

                    val drugId = drug.drug.drugData?.id ?: ""
                    val drugLogId =
                        when (val result = firebaseDataRepository.getDrugLogId(drugId)) {
                            is Result.Success -> result.data
                            else -> null
                        }

                    firebaseDataRepository.postDrugLog(
                        drugId, DrugLog(
                            id = drugLogId,
                            timeTag = drug.timeInt,
                            result = 0,
                            createdTime = Timestamp.now()
                        )
                    )

                    swipeCheckedListManager.addDrugLogId(drugLogId ?: "")

                    val originStock = drug.drug.drugData?.stock ?: 0F
                    val updateStock = originStock - (drug.drug.drugData?.dose ?: 0F)
                    firebaseDataRepository.editStock(drugId, updateStock)
                }
                ItemType.EVENT -> {
                    val event =
                        swipeCheckedListManager.getEventSwipeData()
                            .itemDataType as ItemDataType.EventType

                    firebaseDataRepository.postEventLog(
                        event.event.eventData?.id!!, EventLog(
                            timeTag = event.timeInt,
                            result = 0,
                            createdTime = Timestamp.now()
                        )
                    )
                }
                else -> {
                }
            }
        }
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