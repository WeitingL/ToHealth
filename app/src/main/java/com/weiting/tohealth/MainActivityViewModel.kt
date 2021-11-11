package com.weiting.tohealth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.UserManager
import kotlinx.coroutines.launch

class MainActivityViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val _memberIdList = MutableLiveData<MutableList<String>>()
    val memberIdList: LiveData<MutableList<String>>
        get() = _memberIdList

    private val memberList = mutableListOf<String>()

    init {
        getMemberIdList()
    }

    private fun getMemberIdList() {
        viewModelScope.launch {
            val auth = Firebase.auth

            val user = firebaseDataRepository.getUser(auth.currentUser?.uid!!)
            val groupIdList = user.groupList

            groupIdList.forEach {
                firebaseDataRepository.getMember(it).forEach { member ->
                    memberList.add(member.userId!!)
                }
            }
            _memberIdList.value = memberList
        }

    }
}