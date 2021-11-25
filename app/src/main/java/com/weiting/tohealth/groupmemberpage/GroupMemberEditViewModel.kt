package com.weiting.tohealth.groupmemberpage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Member

class GroupMemberEditViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    member: Member,
    private val groupId: String
) : ViewModel() {

    private val privateSet = MutableLiveData<Int>()

    init {
        privateSet.value = member.private?:0
    }

    fun postNewNickName(member: Member) {
        member.private = privateSet.value
        firebaseDataRepository.updateMemberInfo(groupId, member)
    }

    fun getNewPrivateSet(int: Int) {
        privateSet.value = int
    }
}
