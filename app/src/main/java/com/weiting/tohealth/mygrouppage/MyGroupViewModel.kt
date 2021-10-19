package com.weiting.tohealth.mygrouppage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.Group

class GroupViewModel : ViewModel() {

    private val _groupItemList = MutableLiveData<List<GroupPageItem>>()
    val groupItemList: LiveData<List<GroupPageItem>>
        get() = _groupItemList

    private val list = listOf<GroupPageItem>(GroupPageItem.AddGroups)

    init {
        _groupItemList.value = list
    }

}

sealed class GroupPageItem {

    data class MyGroups(val group: Group) : GroupPageItem()

    object AddGroups : GroupPageItem()

}