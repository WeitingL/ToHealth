package com.weiting.tohealth.mygrouppage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.CalenderItem
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.data.Member
import com.weiting.tohealth.data.Note

class GroupViewModel : ViewModel() {

    private val _groupItemList = MutableLiveData<List<GroupPageItem>>()
    val groupItemList: LiveData<List<GroupPageItem>>
        get() = _groupItemList

    private val list = listOf<GroupPageItem>(
        GroupPageItem.MyGroups(
            Group(
                id = "WOW",
                groupName = "GOOD",
                member = listOf(
                    Member(
                        id = "wetiting",
                        userId = "??!",
                        nickName = "尉庭",
                        private = 2
                    ),
                    Member(
                        id = "wetiting",
                        userId = "??!",
                        nickName = "尉庭",
                        private = 2
                    )
                ),
                notes = listOf(
                    Note(
                        id = ">O<",
                        title = "TEST!",
                        content = "這裡要塞很多東西",
                        editor = "Weiting",
                        footer = 2,
                        createTimestamp = Timestamp.now()
                    ),
                    Note(
                        id = ">O<",
                        title = "TEST!",
                        content = "這裡要塞很多東西",
                        editor = "Weiting",
                        footer = 2,
                        createTimestamp = Timestamp.now()
                    )
                ),
                calenderItems = listOf(
                    CalenderItem(
                        id = "= =",
                        editor = "Me",
                        content = "Wow",
                        date = Timestamp.now(),
                        createTime = Timestamp.now(),
                        result = 2
                    ),
                    CalenderItem(
                        id = "= =",
                        editor = "Me",
                        content = "Wow",
                        date = Timestamp.now(),
                        createTime = Timestamp.now(),
                        result = 2
                    )
                )
            )
        ), GroupPageItem.AddGroups
    )

    init {
        _groupItemList.value = list
    }

}

sealed class GroupPageItem {

    data class MyGroups(val group: Group) : GroupPageItem()
    object AddGroups : GroupPageItem()

}