package com.weiting.tohealth.grouppage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.data.Member
import com.weiting.tohealth.data.Note

class GroupViewModel : ViewModel() {

    private val _groupItemList = MutableLiveData<List<GroupPageItem>>()
    val groupItemList: LiveData<List<GroupPageItem>>
        get() = _groupItemList

    private val list = listOf<GroupPageItem>()

    init {
        _groupItemList.value = list
    }

}

sealed class GroupPageItem {

    data class MyGroups(val group: Group) : GroupPageItem()

    object AddGroups : GroupPageItem()

}