package com.weiting.tohealth.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.*
import kotlinx.coroutines.*

enum class HomeItemDataType(dataType: ItemDataType) {
    DRUG(ItemDataType.DrugType(ItemData())),
    MEASURE(ItemDataType.MeasureType(ItemData())),
    ACTIVITY(ItemDataType.ActivityType(ItemData())),
    CARE(ItemDataType.CareType(ItemData()))
}

class HomeViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val _nextTaskList = MutableLiveData<List<HomePageItem>>()
    val nextTaskList: LiveData<List<HomePageItem>>
        get() = _nextTaskList

    private val _itemDataTypeList = MutableLiveData<List<ItemDataType>>()
    val itemDataTypeList: LiveData<List<ItemDataType>>
        get() = _itemDataTypeList

    private val timestampList = mutableListOf<Timestamp>()
    private val itemList = mutableListOf<ItemDataType>()

//    val drugList = firebaseDataRepository.getLiveDrugList(UserManager.userId)
//    val measureList = firebaseDataRepository.getLiveMeasureList(UserManager.userId)
//    val activityList = firebaseDataRepository.getLiveActivityList(UserManager.userId)
//    val careList = firebaseDataRepository.getLiveCareList(UserManager.userId)

    init {
        _nextTaskList.value = listOf(
            HomePageItem.AddNewItem,
            HomePageItem.TodayAbstract
        )
    }

    fun getItemDataIntoHomePageItem(list: List<ItemDataType>) {
        if (list.isNotEmpty()) {
            _nextTaskList.value = _nextTaskList.value?.plus(HomePageItem.NextTask(list))
        }
    }

    fun addInItemDataTypeList(list: List<*>) {

    }
}

sealed class HomePageItem() {

    object AddNewItem : HomePageItem()
    object TodayAbstract : HomePageItem()
    data class NextTask(val list: List<ItemDataType>) : HomePageItem()
}

sealed class ItemDataType() {
    data class TimeType(val time: String) : ItemDataType()
    data class DrugType(val drug: ItemData) : ItemDataType()
    data class MeasureType(val measure: ItemData) : ItemDataType()
    data class ActivityType(val activity: ItemData) : ItemDataType()
    data class CareType(val care: ItemData) : ItemDataType()
}