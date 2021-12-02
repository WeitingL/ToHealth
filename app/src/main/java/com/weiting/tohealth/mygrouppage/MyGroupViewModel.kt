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

    val userData = firebaseDataRepository.getLiveUser(UserManager.UserInfo.id ?: "")

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
                val groupList = when (val result = firebaseDataRepository.getGroups(id)) {
                    is Result.Success -> result.data
                    else -> null
                }

                if (groupList?.isNotEmpty() == true) {
                    groupList.forEach {

                        it.member += when (val result =
                            firebaseDataRepository.getMembers(it.id ?: "")) {
                            is Result.Success -> result.data
                            else -> listOf()
                        }

                        it.member.forEach { member ->
                            val user = when (val result =
                                firebaseDataRepository.getUser(member.userId ?: "")) {
                                is Result.Success -> result.data
                                else -> null
                            }
                            member.profilePhoto = user?.userPhoto
                        }

                        it.notes += when (val result =
                            firebaseDataRepository.getNotes(it.id ?: "")) {
                            is Result.Success -> result.data
                            else -> listOf()
                        }

                        it.notes.forEach { note ->
                            val user = when (val result =
                                firebaseDataRepository.getUser(note.editor ?: "")) {
                                is Result.Success -> result.data
                                else -> null
                            }
                            note.editor = user?.name
                        }

                        it.reminders += when (val result =
                            firebaseDataRepository.getReminders(it.id ?: "")) {
                            is Result.Success -> result.data
                            else -> listOf()
                        }

                        it.reminders.forEach { calenderItem ->
                            val user = when (val result =
                                firebaseDataRepository.getUser(calenderItem.editor ?: "")) {
                                is Result.Success -> result.data
                                else -> null
                            }
                            calenderItem.editor = user?.name
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
