package com.weiting.tohealth.mygrouppage.dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiting.tohealth.data.*
import kotlinx.coroutines.launch

enum class HowToAddGroup {
    CREATE, JOIN
}

class AddGroupViewModel(
    private val firebaseRepository: FirebaseRepository,
    howToAddGroup: HowToAddGroup
) : ViewModel() {

    private val _newGroupId = MutableLiveData<String>()
    val newGroupId: LiveData<String>
        get() = _newGroupId

    private val _isGroupExist = MutableLiveData<Boolean?>()
    val isGroupExist: LiveData<Boolean?>
        get() = _isGroupExist

    private val _isRelationshipExist = MutableLiveData<Boolean?>()
    val isRelationshipExist: LiveData<Boolean?>
        get() = _isRelationshipExist

    init {
        when (howToAddGroup) {
            HowToAddGroup.JOIN -> {
                _isGroupExist.value = null
                _isRelationshipExist.value = null
            }

            HowToAddGroup.CREATE -> {
                getGroupId()
            }
        }
    }

    private fun getGroupId() {
        _newGroupId.value = firebaseRepository.getNewGroupId()
    }

    fun createGroup(group: Group) {
        firebaseRepository.createGroup(group)
        firebaseRepository.joinGroup(
            Member(
                userId = UserManager.UserInformation.id,
                private = 0,
                name = UserManager.UserInformation.name,
                nickName = UserManager.UserInformation.name
            ), group.id!!
        )
    }

    fun checkIsGroupIdExist(groupId: String) {
        viewModelScope.launch {
            when (firebaseRepository.checkIsGroupExist(groupId)) {
                true -> _isGroupExist.value = true
                false -> _isGroupExist.value = false
            }
        }
    }

    fun checkIsRelationshipExist(
        userId: String,
        groupId: String
    ) {
        viewModelScope.launch {
            when (firebaseRepository.checkIsRelationExist(
                userId, groupId
            )) {
                true -> _isRelationshipExist.value = true
                false -> _isRelationshipExist.value = false
            }
        }
    }

    fun joinGroup(
        member: Member,
        groupId: String
    ) {
        firebaseRepository.joinGroup(
            member,
            groupId
        )
    }


}