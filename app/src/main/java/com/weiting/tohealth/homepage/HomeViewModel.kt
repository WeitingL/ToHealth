package com.weiting.tohealth.homepage

import android.util.Log
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

    private val _postCheckedLog = MutableLiveData<Boolean>()
    val postCheckedLog: LiveData<Boolean>
        get() = _postCheckedLog

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
        _postCheckedLog.value = false

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
    private var liveMeasures =
        firebaseDataRepository.getLiveMeasures(UserManager.UserInfo.id ?: "")
    private var liveEvents =
        firebaseDataRepository.getLiveEvents(UserManager.UserInfo.id ?: "")
    private var liveCares =
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

        addSource(liveMeasures) { measureList ->
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
                        firebaseDataRepository.getMeasureLogs(measure.id!!).filter {
                                Util.isToday(it.createdTime)
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

        addSource(liveEvents) { eventList ->
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
                        firebaseDataRepository.getEventLogs(event.id!!).filter {
                                Util.isToday(it.createdTime)
                            }

                    val logTimeTags = mutableListOf<Int>()
                    event.eventLogs.forEach { eventLog ->
                        logTimeTags.add(eventLog.timeTag!!)
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

        addSource(liveCares) { careList ->
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
                            Util.isToday(it.createdTime)
                        }

                    val logTimeTags = mutableListOf<Int>()
                    care.careLogs.forEach { careLog ->
                        logTimeTags.add(careLog.timeTag ?: 0)
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

    private val swipeSkipListManager = SwipeSkipListManager()
    // Collected the skip item data and time title data.


    fun swipeToSkip(swipeData: SwipeData) {
        _completedTask.value = _completedTask.value?.plus(1)
        swipeSkipListManager.getSwipeToSkip(swipeData)
    }

    fun removeTimeHeader(swipeData: SwipeData) {
        swipeSkipListManager.getSwipeToSkip(swipeData)
    }

    fun undoSwipeToSkip() {
        val currentList = itemDataMediator.value
        _completedTask.value = _completedTask.value?.minus(1)
        itemDataMediator.value =
            swipeSkipListManager.reBuildCurrentList(currentList ?: mutableListOf())
    }

    fun postSkipLog() {
        viewModelScope.launch {
            val skipData = swipeSkipListManager.skipList.first()
            progressCounter.postLog(skipData)
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
                    firebaseDataRepository.postMeasureLog(
                        skipData.itemDataType.measure.measureData?.id!!, MeasureLog(
                            id = firebaseDataRepository
                                .getMeasureLogId(skipData.itemDataType.measure.measureData.id!!),
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
                listManager.removeTimeTitle((it.itemDataType as ItemDataType.TimeType).timeInt)
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
        _completedTask.value = _completedTask.value?.plus(1)

        if (swipeData.itemDataType is ItemDataType.DrugType) {
            swipeCheckedListManager.getDrugSwipeData(swipeData)
            progressCounter.postLog(swipeData)
            postFinishDrugAndActivityLog(ItemType.DRUG)
        } else {
            swipeCheckedListManager.getEventSwipeData(swipeData)
            progressCounter.postLog(swipeData)
        }

    }

    fun removeTimeHeaderOfFinished(swipeData: SwipeData) {
        swipeCheckedListManager.getEventTimeHeader(swipeData)
    }

    fun undoSwipeToLog(itemType: ItemType) {
        when (itemType) {
            ItemType.DRUG -> {
                _completedTask.value = _completedTask.value?.minus(1)

                val logId = swipeCheckedListManager.giveDrugLogIdForUndo()
                val drugData = swipeCheckedListManager
                    .giveDrugSwipeDataForUndo().itemDataType as ItemDataType.DrugType
                val drugId = drugData.drug.drugData?.id ?: ""
                val drugStock = drugData.drug.drugData?.stock ?: 0F

                firebaseDataRepository.deleteDrugLog(drugId, logId)
                firebaseDataRepository.editStock(drugId, drugStock)

            }
            ItemType.EVENT -> {
                val currentList = itemDataMediator.value
                _completedTask.value = _completedTask.value?.minus(1)
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
                        swipeCheckedListManager.giveDrugSwipeDataForPost()
                            .itemDataType as ItemDataType.DrugType

                    if (isDrugExhausted(drug.drug.drugData ?: Drug())) {
                        postDrugExhausted(drug.drug.drugData ?: Drug())
                    }

                    val drugId = drug.drug.drugData?.id ?: ""
                    val drugLogId = firebaseDataRepository.getDrugLogId(drugId)

                    firebaseDataRepository.postDrugLog(
                        drugId, DrugLog(
                            id = drugLogId,
                            timeTag = drug.timeInt,
                            result = 0,
                            createdTime = Timestamp.now()
                        )
                    )

                    swipeCheckedListManager.getDrugLogId(drugLogId)

                    val originStock = drug.drug.drugData?.stock ?: 0F
                    val updateStock = originStock - (drug.drug.drugData?.dose ?: 0F)
                    firebaseDataRepository.editStock(drugId, updateStock)
                }
                ItemType.EVENT -> {
                    val event =
                        swipeCheckedListManager.giveEventSwipeData().itemDataType as ItemDataType.EventType

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