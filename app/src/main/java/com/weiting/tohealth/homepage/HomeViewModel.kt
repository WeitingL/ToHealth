package com.weiting.tohealth.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.*
import com.weiting.tohealth.toTimeFromTimeStamp
import kotlinx.coroutines.*
import java.util.*

class HomeViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val _nextTaskList = MutableLiveData<List<HomePageItem>>()
    val nextTaskList: LiveData<List<HomePageItem>>
        get() = _nextTaskList

//    private val _itemDataTypeList = MutableLiveData<List<ItemDataType>>()
//    val itemDataTypeList: LiveData<List<ItemDataType>>
//        get() = _itemDataTypeList



    private val itemList = mutableListOf<ItemDataType>()

    private val timeIntList = mutableListOf<Int>()
    private val timeList = mutableListOf<ItemDataType>()
    private val drugItemList = mutableListOf<ItemDataType>()
    private val measureItemList = mutableListOf<ItemDataType>()
    private val activityItemList = mutableListOf<ItemDataType>()
    private val careItemList = mutableListOf<ItemDataType>()



    private val finalItemList = mutableListOf<ItemDataType>()

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
        if (list.isNotEmpty()) {
            _nextTaskList.value = _nextTaskList.value?.plus(HomePageItem.NextTask(list))
        }
    }

    fun getDrugs(list: List<Drug>) {
        drugItemList.clear()
        list.forEach { drug ->

            drug.executeTime.forEach {
                val c = Calendar.getInstance()
                c.time = it.toDate()
                val timeInt = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE)

                drugItemList.add(ItemDataType.DrugType(ItemData(DrugData = drug), timeInt))

                if (timeInt !in timeIntList) {
                    timeList.add(ItemDataType.TimeType(toTimeFromTimeStamp(it), timeInt))
                    timeIntList.add(timeInt)
                    timeIntList.sort()
                }
            }
        }
        reArrangeItemDataType()
    }

    fun getMeasures(list: List<Measure>) {
        measureItemList.clear()
        list.forEach { measure ->

            measure.executeTime.forEach {
                val c = Calendar.getInstance()
                c.time = it.toDate()
                val timeInt = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE)

                measureItemList.add(ItemDataType.MeasureType(ItemData(MeasureData = measure), timeInt))

                if (timeInt !in timeIntList) {
                    timeList.add(ItemDataType.TimeType(toTimeFromTimeStamp(it), timeInt))
                    timeIntList.add(timeInt)
                    timeIntList.sort()
                }
            }
        }
        reArrangeItemDataType()
    }

    fun getActivity(list: List<Activity>) {
        activityItemList.clear()
        list.forEach { activity ->

            activity.executeTime.forEach {
                val c = Calendar.getInstance()
                c.time = it.toDate()
                val timeInt = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE)

                activityItemList.add(ItemDataType.ActivityType(ItemData(ActivityData = activity), timeInt))

                if (timeInt !in timeIntList) {
                    timeList.add(ItemDataType.TimeType(toTimeFromTimeStamp(it), timeInt))
                    timeIntList.add(timeInt)
                    timeIntList.sort()
                }
            }
        }
        reArrangeItemDataType()
    }

    fun getCares(list: List<Care>) {
        careItemList.clear()
        list.forEach { care ->
            care.executeTime.forEach {
                val c = Calendar.getInstance()
                c.time = it.toDate()
                val timeInt = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE)

                careItemList.add(ItemDataType.CareType(ItemData(CareData = care), timeInt))

                if (timeInt !in timeIntList) {
                    timeList.add(ItemDataType.TimeType(toTimeFromTimeStamp(it), timeInt))
                    timeIntList.add(timeInt)
                    timeIntList.sort()
                }
            }
        }
        reArrangeItemDataType()
    }

    private fun reArrangeItemDataType() {
        finalItemList.clear()

        timeIntList.forEach { intTime ->
            timeList.forEach { timeItem ->
                if ((timeItem as ItemDataType.TimeType).timeInt == intTime) {
                    finalItemList.add(timeItem)

                    drugItemList.forEach { durg ->
                        if ((durg as ItemDataType.DrugType).timeInt == intTime){
                            if (durg.timeInt == intTime) {
                                finalItemList.add(durg)
                            }
                        }
                    }

                    measureItemList.forEach { measure ->
                        if ((measure as ItemDataType.MeasureType).timeInt == intTime){
                            if (measure.timeInt == intTime) {
                                finalItemList.add(measure)
                            }
                        }
                    }

                    activityItemList.forEach {activity ->
                        if ((activity as ItemDataType.ActivityType).timeInt == intTime){
                            if (activity.timeInt == intTime) {
                                finalItemList.add(activity)
                            }
                        }
                    }

                    careItemList.forEach { care ->
                        if ((care as ItemDataType.CareType).timeInt == intTime){
                            if (care.timeInt == intTime) {
                                finalItemList.add(care)
                            }
                        }
                    }
                }
            }
        }

        if (finalItemList.isEmpty()){
            _nextTaskList.value = listOf(
                HomePageItem.AddNewItem,
                HomePageItem.TodayAbstract
            )
        }else{
            _nextTaskList.value = listOf(
                HomePageItem.AddNewItem,
                HomePageItem.TodayAbstract,
                HomePageItem.NextTask(finalItemList)
            )
        }
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