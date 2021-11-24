package com.weiting.tohealth.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Member
import com.weiting.tohealth.groupmemberpage.GroupMemberEditViewModel

class GroupMemberEditViewModelFactory(
    private val firebaseDataRepository: FirebaseRepository,
    private val member: Member,
    private val groupId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(GroupMemberEditViewModel::class.java) ->
                    GroupMemberEditViewModel(firebaseDataRepository, member, groupId)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
