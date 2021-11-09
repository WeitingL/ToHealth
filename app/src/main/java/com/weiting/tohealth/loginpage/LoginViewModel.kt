package com.weiting.tohealth.loginpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.User
import com.weiting.tohealth.data.UserManager
import kotlinx.coroutines.launch

class LoginViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val _userInfo = MutableLiveData<User>()
    val userInfo: LiveData<User>
        get() = _userInfo

    fun signInUserInfo(user: User) {
        viewModelScope.launch {
            val userExist = firebaseDataRepository.getUserInfo(user.id!!)

            if (userExist.id == null){
                firebaseDataRepository.signIn(user)
                UserManager.UserInformation = user
            }else{
                initialUserManager(user.id)
            }
        }
    }

    fun initialUserManager(userId: String) {
        viewModelScope.launch {
            _userInfo.value = firebaseDataRepository.getUserInfo(userId)
//            Log.i("data", UserManager.UserInformation.toString())
        }
    }
}