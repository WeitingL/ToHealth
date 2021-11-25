package com.weiting.tohealth.mymanagepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.*

class ManageDetailViewModel(
    firebaseDataRepository: FirebaseRepository,
    user: User
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

    val drugList = firebaseDataRepository.getLiveDrugs(user.id!!)
    val measureList = firebaseDataRepository.getLiveMeasures(user.id!!)
    val activityList = firebaseDataRepository.getLiveEvents(user.id!!)
    val careList = firebaseDataRepository.getLiveCares(user.id!!)

    fun putInDetailList(list: List<ItemData>) {
        if (!list.isNullOrEmpty()) {
            _isTheNewBie.value = false
        }
        _manageDetailList.value = list
    }
}
