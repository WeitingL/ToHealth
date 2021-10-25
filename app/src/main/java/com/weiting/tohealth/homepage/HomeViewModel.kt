package com.weiting.tohealth.homepage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.*
import com.weiting.tohealth.toTimeFromTimeStamp
import kotlinx.coroutines.*

enum class HomeItemDataType(dataType: ItemDataType){
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

    val drugList = firebaseDataRepository.getLiveDrugList(UserManager.userId)
    val measureList = firebaseDataRepository.getLiveMeasureList(UserManager.userId)
    val activityList = firebaseDataRepository.getLiveActivityList(UserManager.userId)
    val careList = firebaseDataRepository.getLiveCareList(UserManager.userId)

    init {
        _nextTaskList.value = listOf(
            HomePageItem.AddNewItem,
            HomePageItem.TodayAbstract
        )
    }

    fun getItemDataIntoHomePageItem(list: List<ItemDataType>) {
        if(list.isNotEmpty()){
            _nextTaskList.value = _nextTaskList.value?.plus(HomePageItem.NextTask(list))
        }
    }

    fun addInItemDataTypeList(list: List<*>){

    }

    private fun getAllItemsByUserId() {

        arrangeItemsByTime(
            drugList.value ?: listOf(),
            measureList.value ?: listOf(),
            activityList.value ?: listOf(),
            careList.value ?: listOf()
        )
    }

    private fun getTimeList(timestamp: Timestamp) {
        when (timestamp in timestampList) {
            true -> {
            }
            false -> {
                timestampList.add(timestamp)
            }
        }
//        Log.i("before sort", timestampList.toString())
        timestampList.sort()
//        Log.i("after sort", timestampList.toString())
    }

    private fun getItemData(itemDataType: ItemDataType) {
        itemList.add(itemDataType)
//        Log.i("?!", itemList.toString())
    }

    private fun arrangeItemsByTime(
        drugList: List<Drug>,
        measureList: List<Measure>,
        activityList: List<Activity>,
        careList: List<Care>
    ) {
        drugList.forEach {
            getTimeList(it.firstTimePerDay!!)
        }
        measureList.forEach {
            getTimeList(it.firstTimePerDay!!)
        }
        activityList.forEach {
            getTimeList(it.firstTimePerDay!!)
        }
        careList.forEach {
            getTimeList(it.firstTimePerDay!!)
        }

        timestampList.forEach { time ->
            getItemData(ItemDataType.TimeType(toTimeFromTimeStamp(time)))

            drugList.forEach {
                if (it.firstTimePerDay!! == time) {
                    getItemData(ItemDataType.DrugType(ItemData(DrugData = it)))
                }
            }

            measureList.forEach {
                if (it.firstTimePerDay!! == time) {
                    getItemData(ItemDataType.MeasureType(ItemData(MeasureData = it)))
                }
            }

            activityList.forEach {
                if (it.firstTimePerDay!! == time) {
                    getItemData(ItemDataType.ActivityType(ItemData(ActivityData = it)))
                }
            }

            careList.forEach {
                if (it.firstTimePerDay!! == time) {
                    getItemData(ItemDataType.CareType(ItemData(CareData = it)))
                }
            }
        }
        _itemDataTypeList.value = itemList
    }
}

sealed class HomePageItem() {

    object AddNewItem : HomePageItem()
    object TodayAbstract : HomePageItem()
    object MyGroupNews : HomePageItem()
    data class NextTask(val list: List<ItemDataType>) : HomePageItem()
}

sealed class ItemDataType() {

    data class TimeType(val time: String) : ItemDataType()
    data class DrugType(val drug: ItemData) : ItemDataType()
    data class MeasureType(val measure: ItemData) : ItemDataType()
    data class ActivityType(val activity: ItemData) : ItemDataType()
    data class CareType(val care: ItemData) : ItemDataType()
}