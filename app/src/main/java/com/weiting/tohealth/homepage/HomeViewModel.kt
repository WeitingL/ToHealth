package com.weiting.tohealth.homepage

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.*
import kotlinx.coroutines.*
import kotlinx.parcelize.Parcelize

class HomeViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val _nextTaskList = MutableLiveData<List<HomePageItem>>()
    val nextTaskList: LiveData<List<HomePageItem>>
        get() = _nextTaskList

    private val _navigateToDialog = MutableLiveData<SwipeInfo>()
    val navigateToDialog: LiveData<SwipeInfo>
        get() = _navigateToDialog

    private val _navigateToSkip = MutableLiveData<SwipeInfo>()
    val navigateToSkip: LiveData<SwipeInfo>
        get() = _navigateToSkip

    private val todoListGenerator = TodoListGenerator()

    val drugList = firebaseDataRepository.getLiveDrugList(UserManager.userId)
    val measureList = firebaseDataRepository.getLiveMeasureList(UserManager.userId)
    val activityList = firebaseDataRepository.getLiveActivityList(UserManager.userId)
    val careList = firebaseDataRepository.getLiveCareList(UserManager.userId)

    val careScore = MutableLiveData<Int>()
    val careInfo = MutableLiveData<String>()


    init {
        _nextTaskList.value = listOf(
            HomePageItem.AddNewItem,
            HomePageItem.TodayAbstract
        )
    }


    /*
        Temporary storage ItemLog in VM (for undo) ->
        Remove the item from final list ->
        when home fragment onDestroy, the ItemLog will post to firebase.
     */

    fun swipeToNavigate(itemDataType: ItemDataType, positionOfItem: Int) {
        _navigateToDialog.value = SwipeInfo(itemDataType, positionOfItem)
    }

    fun swipeToSkip(itemDataType: ItemDataType, positionOfItem: Int) {
        _navigateToSkip.value = SwipeInfo(itemDataType, positionOfItem)
    }


    fun getDrugs(list: List<Drug>) {
        viewModelScope.launch {

            // Get the itemLog
            list.forEach {
                val drugLog = firebaseDataRepository.getDrugRecord(it.id!!, Timestamp.now())
                it.drugLogs = drugLog
            }

            todoListGenerator.getDrugs(list)
            if (todoListGenerator.finalItemList.isEmpty()) {
                _nextTaskList.value = listOf(
                    HomePageItem.AddNewItem,
                    HomePageItem.TodayAbstract
                )
            } else {
                _nextTaskList.value = listOf(
                    HomePageItem.AddNewItem,
                    HomePageItem.TodayAbstract,
                    HomePageItem.NextTask(todoListGenerator.finalItemList)
                )
            }
        }
    }

    fun getMeasures(list: List<Measure>) {
        viewModelScope.launch {
            // Get the itemLog
            list.forEach {
                val measureLog = firebaseDataRepository.getMeasureRecord(it.id!!, Timestamp.now())
                it.measureLogs = measureLog
            }

            todoListGenerator.getMeasures(list)
            if (todoListGenerator.finalItemList.isEmpty()) {
                _nextTaskList.value = listOf(
                    HomePageItem.AddNewItem,
                    HomePageItem.TodayAbstract
                )
            } else {
                _nextTaskList.value = listOf(
                    HomePageItem.AddNewItem,
                    HomePageItem.TodayAbstract,
                    HomePageItem.NextTask(todoListGenerator.finalItemList)
                )
            }
        }
    }

    fun getActivity(list: List<Activity>) {
        viewModelScope.launch {

            // Get the itemLog
            list.forEach {
                val activityLog = firebaseDataRepository.getActivityRecord(it.id!!, Timestamp.now())
                it.activityLogs = activityLog
            }

            todoListGenerator.getActivity(list)
            if (todoListGenerator.finalItemList.isEmpty()) {
                _nextTaskList.value = listOf(
                    HomePageItem.AddNewItem,
                    HomePageItem.TodayAbstract
                )
            } else {
                _nextTaskList.value = listOf(
                    HomePageItem.AddNewItem,
                    HomePageItem.TodayAbstract,
                    HomePageItem.NextTask(todoListGenerator.finalItemList)
                )
            }
        }
    }

    fun getCares(list: List<Care>) {
        viewModelScope.launch {

            // Get the itemLog
            list.forEach {
                val careLog = firebaseDataRepository.getCareRecord(it.id!!, Timestamp.now())
                it.careLogs = careLog
            }

            todoListGenerator.getCares(list)
            if (todoListGenerator.finalItemList.isEmpty()) {
                _nextTaskList.value = listOf(
                    HomePageItem.AddNewItem,
                    HomePageItem.TodayAbstract
                )
            } else {
                _nextTaskList.value = listOf(
                    HomePageItem.AddNewItem,
                    HomePageItem.TodayAbstract,
                    HomePageItem.NextTask(todoListGenerator.finalItemList)
                )
            }
        }
    }

    fun getCareScore(int: Int) {
        careScore.value = int
    }

    fun getInfo(note: String) {
        careInfo.value = note
    }

    fun getItemLog(itemLogData: ItemLogData, positionOfItem: Int) {
        todoListGenerator.getLog(itemLogData, positionOfItem)
        if (todoListGenerator.finalItemList.isEmpty()) {
            _nextTaskList.value = listOf(
                HomePageItem.AddNewItem,
                HomePageItem.TodayAbstract
            )
        } else {
            _nextTaskList.value = listOf(
                HomePageItem.AddNewItem,
                HomePageItem.TodayAbstract,
                HomePageItem.NextTask(todoListGenerator.finalItemList)
            )
        }
    }

    fun removeLast() {
        todoListGenerator.removeLastLog()
        if (todoListGenerator.finalItemList.isEmpty()) {
            _nextTaskList.value = listOf(
                HomePageItem.AddNewItem,
                HomePageItem.TodayAbstract
            )
        } else {
            _nextTaskList.value = listOf(
                HomePageItem.AddNewItem,
                HomePageItem.TodayAbstract,
                HomePageItem.NextTask(todoListGenerator.finalItemList)
            )
        }
    }

    //Post Done Record to firebase
    fun postRecord() {
        todoListGenerator.logList.forEach {
            when (it.ItemType) {
                ItemType.DRUG -> {
                    firebaseDataRepository.postDrugRecord(
                        it.ItemId!!, it.DrugLog!!
                    )
                }
                ItemType.MEASURE -> {
                    firebaseDataRepository.postMeasureRecord(
                        it.ItemId!!, it.MeasureLog!!
                    )
                }
                ItemType.ACTIVITY -> {
                    firebaseDataRepository.postActivityRecord(
                        it.ItemId!!, it.ActivityLog!!
                    )
                }
                ItemType.CARE -> {
                    firebaseDataRepository.postCareRecord(
                        it.ItemId!!, it.CareLog!!
                    )
                }
            }
        }
    }

    fun getOutDateTimeHeader(itemDataType: ItemDataType, positionOfItem: Int) {
        todoListGenerator.getOutTimeHeader(itemDataType, positionOfItem)
    }

}

sealed class HomePageItem() {

    object AddNewItem : HomePageItem()
    object TodayAbstract : HomePageItem()
    data class NextTask(val list: List<ItemDataType>) : HomePageItem()
}

sealed class ItemDataType() {

    data class TimeType(val time: String, val timeInt: Int) : ItemDataType()
    data class DrugType(val drug: ItemData, val timeInt: Int) : ItemDataType()
    data class MeasureType(val measure: ItemData, val timeInt: Int) : ItemDataType()
    data class ActivityType(val activity: ItemData, val timeInt: Int) : ItemDataType()
    data class CareType(val care: ItemData, val timeInt: Int) : ItemDataType()
}

data class SwipeInfo(
    val itemDataType: ItemDataType,
    val positionOfItem: Int
)