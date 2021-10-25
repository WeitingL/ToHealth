package com.weiting.tohealth.mygrouppage.grouproom.members

import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Group

class MembersViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val group: Group
) : ViewModel() {

    val liveMembersList = firebaseDataRepository.getLiveMember(group.id!!)

}