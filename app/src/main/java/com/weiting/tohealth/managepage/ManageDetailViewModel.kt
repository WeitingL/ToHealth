package com.weiting.tohealth.managepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ManageDetailViewModel : ViewModel() {

    private val _manageDetailList = MutableLiveData<List<ManageDetailItems>>()
    val manageDetailList: LiveData<List<ManageDetailItems>>
        get() = _manageDetailList

    private val list = listOf<ManageDetailItems>(
        ManageDetailItems.DetailItems, ManageDetailItems.DetailItems
    )

    init {
        _manageDetailList.value = list
    }

}

sealed class ManageDetailItems {
    object DetailItems : ManageDetailItems()
}