package com.weiting.tohealth.loginpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.User
import kotlinx.coroutines.launch

class LoginViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val _userInfo = MutableLiveData<User>()
    val userInfo: LiveData<User>
        get() = _userInfo

    fun signInUserInfo(user: User) {
        viewModelScope.launch {
            val userExist = firebaseDataRepository.getUser(user.id!!)

            if (userExist.id.isNullOrEmpty()) {
                firebaseDataRepository.signIn(user)
            }

            initialUserManager(user.id!!)

        }
    }

    private fun initialUserManager(userId: String) {
        viewModelScope.launch {
            _userInfo.value = firebaseDataRepository.getUser(userId)
        }
    }
}