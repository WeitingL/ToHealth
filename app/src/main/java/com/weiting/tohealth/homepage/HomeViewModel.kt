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

class HomeViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val _nextTaskList = MutableLiveData<List<HomePageItem>>()
    val nextTaskList: LiveData<List<HomePageItem>>
        get() = _nextTaskList

    private val _List = MutableLiveData<List<ItemDataType>>()
    val List: LiveData<List<ItemDataType>>
        get() = _List

    private val list = mutableListOf<ItemDataType>()

    init {
        _nextTaskList.value = listOf(
            HomePageItem.AddNewItem,
            HomePageItem.TodayAbstract
        )
        getAllItemsByUserId()
//        Log.i("Desen", listOf<Int>(3,4,6,2,45,2,4).distinct().sorted().toString())
    }

    private fun getAllItemsByUserId() {
        viewModelScope.launch {
            arrangeItemsByTime(
                firebaseDataRepository.getAllDrugs(),
                firebaseDataRepository.getAllMeasures(),
                firebaseDataRepository.getAllActivities(),
                firebaseDataRepository.getAllCares(),
            )
        }
    }

    private fun arrangeItemsByTime(
        drugList: List<Drug>,
        measureList: List<Measure>,
        activityList: List<Activity>,
        careList: List<Care>
    ) {
        val timeList = mutableListOf<Timestamp>()

        drugList.forEach {
            timeList + it.firstTimePerDay
        }
        measureList.forEach {
            timeList + it.firstTimePerDay
        }
        activityList.forEach {
            timeList + it.firstTimePerDay
        }
        careList.forEach {
            timeList + it.firstTimePerDay
        }
//        timeList.distinct().sorted()

        timeList.forEach { time ->
            list + ItemDataType.TimeType(toTimeFromTimeStamp(time))

            drugList.forEach {
                if (it.firstTimePerDay!! == time) {
                    list + ItemDataType.DrugType(ItemData(DrugData = it))
                }
            }

            measureList.forEach {
                if (it.firstTimePerDay!! == time) {
                    list + ItemDataType.MeasureType(ItemData(MeasureData = it))
                }
            }

            activityList.forEach {
                if (it.firstTimePerDay!! == time) {
                    list + ItemDataType.ActivityType(ItemData(ActivityData = it))
                }
            }

            careList.forEach {
                if (it.firstTimePerDay!! == time) {
                    list + ItemDataType.CareType(ItemData(CareData = it))
                }
            }
        }
    }

    private fun getAllDrugs() {
        viewModelScope.launch {
            val list = firebaseDataRepository.getAllDrugs()

            if (list.isNotEmpty()) {
//                _nextTaskList.value = _nextTaskList.value?.plus(HomePageItem.NextTask(list))
            }
        }
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