package com.weiting.tohealth.mygrouppage.grouproom.members

import androidx.lifecycle.*
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.data.Member
import kotlinx.coroutines.launch

class MembersViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    group: Group
) : ViewModel() {

    private val liveMembersList = firebaseDataRepository.getLiveMember(groupId = group.id!!)

    val memberLive = MediatorLiveData<List<Member>>().apply {
        addSource(liveMembersList) { list ->
            viewModelScope.launch {
                list.forEach { member ->
                    member.profilePhoto = firebaseDataRepository.getUser(member.userId!!).userPhoto
                }
                value = list
            }
        }
    }
}
