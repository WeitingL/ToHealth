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
        _newGroupId.value = when (val result = firebaseRepository.getNewGroupId()) {
            is Result.Success -> result.data
            else -> null
        }
    }

    fun createGroup(group: Group) {
        firebaseRepository.createGroup(group)
        firebaseRepository.joinGroup(
            Member(
                userId = UserManager.UserInfo.id,
                private = 0,
                name = UserManager.UserInfo.name,
                nickName = UserManager.UserInfo.name
            ),
            group.id ?: ""
        )
    }

    fun checkIsGroupIdExist(groupId: String) {
        viewModelScope.launch {
            val isExist = when (val result = firebaseRepository.checkIsGroupExist(groupId)) {
                is Result.Success -> result.data
                else -> null
            }

            when (isExist) {
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
            val isExist =
                when (val result = firebaseRepository.checkIsRelationExist(userId, groupId)) {
                    is Result.Success -> result.data
                    else -> null
                }

            when (isExist) {
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
