package com.weiting.tohealth.mygrouppage

import android.util.Log
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

    val userData = firebaseDataRepository.login(UserManager.UserInformation.id!!)

    init {
        _groupItemList.value = listOf(
            GroupPageItem.AddGroups
        )
    }

    fun getGroup(idList: List<String>) {
        viewModelScope.launch {
            idList.forEach { id ->
                val groupList = firebaseDataRepository.getGroups(id)

                if (groupList.isNotEmpty()) {
                    _groupItemList.value = listOf()
                    groupList.forEach {

                        it.member += firebaseDataRepository.getMember(it.id!!)
                        it.member.forEach { member ->
                            member.profilePhoto =
                                firebaseDataRepository.getUserInfo(member.userId!!).userPhoto
                        }

                        it.notes += firebaseDataRepository.getNote(it.id!!)
                        it.notes.forEach { note ->
                            note.editor = firebaseDataRepository.getUserInfo(note.editor!!).name
                        }

                        it.calenderItems += firebaseDataRepository.getCalenderItem(it.id!!)
                        it.calenderItems.forEach { calenderItem ->
                            calenderItem.editor =
                                firebaseDataRepository.getUserInfo(calenderItem.editor!!).name
                        }

                        _groupItemList.value =
                            _groupItemList.value?.plus(GroupPageItem.MyGroups(it))
                    }
                    _groupItemList.value = _groupItemList.value?.plus(GroupPageItem.AddGroups)
                }
            }
        }
    }
}

sealed class GroupPageItem {

    data class MyGroups(val group: Group) : GroupPageItem()
    object AddGroups : GroupPageItem()

}