package com.weiting.tohealth.mymanagepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.data.FirebaseRepository
import kotlinx.coroutines.launch

class ManageDetailViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val manageType: ManageType
) : ViewModel() {

    private val _manageDetailList = MutableLiveData<List<ItemData>>()
    val manageDetailList: LiveData<List<ItemData>>
        get() = _manageDetailList

    init {
        _manageDetailList.value = listOf()
        getAllOngoingAndSuspendItems()
    }

    private fun getAllOngoingAndSuspendItems() {
        viewModelScope.launch {
            when (manageType) {
                ManageType.DRUG -> {
                    val drugList = firebaseDataRepository.getAllDrugs()
                    for (i in drugList.indices) {
                        _manageDetailList.value =
                            _manageDetailList.value?.plus(ItemData(DrugData = drugList[i]))
                    }
                }

                ManageType.MEASURE -> {
                    val measureList = firebaseDataRepository.getAllMeasures()
                    for (i in measureList.indices) {
                        _manageDetailList.value =
                            _manageDetailList.value?.plus(ItemData(MeasureData = measureList[i]))
                    }
                }

                ManageType.ACTIVITY -> {
                    val activityList = firebaseDataRepository.getAllActivities()
                    for (i in activityList.indices) {
                        _manageDetailList.value =
                            _manageDetailList.value?.plus(ItemData(ActivityData = activityList[i]))
                    }
                }

                ManageType.CARE -> {
                    val careList = firebaseDataRepository.getAllCares()
                    for (i in careList.indices) {
                        _manageDetailList.value =
                            _manageDetailList.value?.plus(ItemData(CareData = careList[i]))
                    }
                }
            }
        }
    }
}