package com.weiting.tohealth.mymanagepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiting.tohealth.data.Drug
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.UserManager
import kotlinx.coroutines.launch

class ManageDetailViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val manageType: ManageType
) : ViewModel() {

    private val _manageDetailList = MutableLiveData<List<ItemData>>()
    val manageDetailList: LiveData<List<ItemData>>
        get() = _manageDetailList

    val drugList = firebaseDataRepository.getLiveDrugList(UserManager.userId)
    val measureList = firebaseDataRepository.getLiveMeasureList(UserManager.userId)
    val activityList = firebaseDataRepository.getLiveActivityList(UserManager.userId)
    val careList = firebaseDataRepository.getLiveCareList(UserManager.userId)

    fun putInDetailList(list: List<ItemData>){
        _manageDetailList.value = list
    }



}