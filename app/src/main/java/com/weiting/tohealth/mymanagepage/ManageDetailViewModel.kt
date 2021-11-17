package com.weiting.tohealth.mymanagepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiting.tohealth.data.*
import kotlinx.coroutines.launch

class ManageDetailViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val manageType: ManageType,
    private val user: User
) : ViewModel() {

    private val _isTheNewBie = MutableLiveData<Boolean>()
    val isTheNewBie: LiveData<Boolean>
        get() = _isTheNewBie

    init {
        _isTheNewBie.value = true
    }

    private val _manageDetailList = MutableLiveData<List<ItemData>>()
    val manageDetailList: LiveData<List<ItemData>>
        get() = _manageDetailList

    val drugList = firebaseDataRepository.getLiveDrugList(user.id!!)
    val measureList = firebaseDataRepository.getLiveMeasureList(user.id!!)
    val activityList = firebaseDataRepository.getLiveActivityList(user.id!!)
    val careList = firebaseDataRepository.getLiveCareList(user.id!!)

    fun putInDetailList(list: List<ItemData>) {
        if (!list.isNullOrEmpty()){
            _isTheNewBie.value = false
        }
        _manageDetailList.value = list
    }

}