package com.weiting.tohealth.groupmemberpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Member

class GroupMemberEditViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    member: Member,
    private val groupId: String
) : ViewModel() {

    private val _privateSet = MutableLiveData<Int>()
    val privateSet: LiveData<Int>
        get() = _privateSet

    init {
        _privateSet.value = member.private!!
    }

    fun postNewNickName(member: Member) {
        member.private = privateSet.value
        firebaseDataRepository.updateMemberInfo(groupId, member)
    }

    fun getNewPrivateSet(int: Int) {
        _privateSet.value = int
    }
}
