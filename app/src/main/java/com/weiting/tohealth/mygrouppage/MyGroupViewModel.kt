package com.weiting.tohealth.mygrouppage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiting.tohealth.data.*
import kotlinx.coroutines.launch

class MyGroupViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val _groupItemList = MutableLiveData<List<GroupPageItem>>()
    val groupItemList: LiveData<List<GroupPageItem>>
        get() = _groupItemList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    val userData = firebaseDataRepository.getLiveUser(UserManager.UserInfo.id!!)

    private val currentGroupList = mutableListOf<GroupPageItem>()

    init {
        _groupItemList.value = listOf(
            GroupPageItem.AddGroups
        )
        _loading.value = true
    }

    fun getGroup(idList: List<String>) {
        viewModelScope.launch {
            _groupItemList.value = mutableListOf()
            currentGroupList.clear()
            idList.forEach { id ->
                val groupList = firebaseDataRepository.getGroups(id)

                if (groupList.isNotEmpty()) {
                    groupList.forEach {

                        it.member += firebaseDataRepository.getMembers(it.id!!)
                        it.member.forEach { member ->
                            member.profilePhoto =
                                firebaseDataRepository.getUser(member.userId!!).userPhoto
                        }

                        it.notes += firebaseDataRepository.getNotes(it.id!!)
                        it.notes.forEach { note ->
                            note.editor = firebaseDataRepository.getUser(note.editor!!).name
                        }

                        it.reminders += firebaseDataRepository.getReminders(it.id!!)
                        it.reminders.forEach { calenderItem ->
                            calenderItem.editor =
                                firebaseDataRepository.getUser(calenderItem.editor!!).name
                        }
                        currentGroupList.add(GroupPageItem.MyGroups(it))
                    }
                }
            }
            _groupItemList.value = currentGroupList.toList()
            _groupItemList.value = _groupItemList.value?.plus(GroupPageItem.AddGroups)
            _loading.value = false
        }
    }
}

sealed class GroupPageItem {

    data class MyGroups(val group: Group) : GroupPageItem()
    object AddGroups : GroupPageItem()
}
