package com.weiting.tohealth.mygrouppage.grouproom.members

import androidx.lifecycle.*
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.data.Member
import com.weiting.tohealth.data.Result
import kotlinx.coroutines.launch

class MembersViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    group: Group
) : ViewModel() {

    private val liveMembersList = firebaseDataRepository.getLiveMembers(groupId = group.id ?: "")

    val memberLive = MediatorLiveData<List<Member>>().apply {
        addSource(liveMembersList) { list ->
            viewModelScope.launch {
                list.forEach { member ->
                    val user = when(val result = firebaseDataRepository.getUser(member.userId ?: "")){
                        is Result.Success -> result.data
                        else -> null
                    }
                    member.profilePhoto = user?.userPhoto
                }
                value = list
            }
        }
    }
}
