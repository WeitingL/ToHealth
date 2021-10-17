package com.weiting.tohealth.grouppage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupViewModel : ViewModel() {

    private val _groupItemList = MutableLiveData<List<GroupPageItem>>()
    val groupItemList: LiveData<List<GroupPageItem>>
        get() = _groupItemList

    val list = listOf<GroupPageItem>(
        GroupPageItem.MyGroups,
        GroupPageItem.MyGroups,
        GroupPageItem.AddGroups
    )

    init {
        _groupItemList.value = list
    }
}

sealed class GroupPageItem {

    object MyGroups : GroupPageItem()

    object AddGroups : GroupPageItem()

}