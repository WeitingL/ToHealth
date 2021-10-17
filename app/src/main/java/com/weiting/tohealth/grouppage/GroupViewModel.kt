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

    private val list = listOf<GroupPageItem>(
        GroupPageItem.MyGroups(
            Group(
                name = "最愛家人",
                code = "MYFAMILY",
                member = listOf(
                    Member(
                        name = "MOM"
                    ),
                    Member(
                        "DAD"
                    ),
                    Member(
                        "DAD"
                    ),
                    Member(
                        "DAD"
                    ),
                    Member(
                        "DAD"
                    )
                ),
                note = listOf(
                    Note(
                        title = "測試資訊",
                        content = "讚讚讚"
                    ),
                    Note(
                        title = "測試資訊",
                        content = "讚讚讚"
                    )
                )
            )
        ),
        GroupPageItem.MyGroups(
            Group(
                name = "最愛家人",
                code = "MYFAMILY",
                member = listOf(
                    Member(
                        name = "MOM"
                    ),
                    Member(
                        "DAD"
                    )
                ),
                note = listOf(
                    Note(
                        title = "測試資訊",
                        content = "讚讚讚"
                    ),
                    Note(
                        title = "測試資訊",
                        content = "讚讚讚"
                    )
                )
            )
        ),
        GroupPageItem.AddGroups
    )

    init {
        _groupItemList.value = list
    }

}

sealed class GroupPageItem {

    data class MyGroups(val group: Group) : GroupPageItem()

    object AddGroups : GroupPageItem()

}