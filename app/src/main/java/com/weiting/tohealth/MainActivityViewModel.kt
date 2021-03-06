package com.weiting.tohealth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Member
import com.weiting.tohealth.data.Result
import com.weiting.tohealth.works.RebuildAlarm
import kotlinx.coroutines.launch

class MainActivityViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean>
        get() = _isLogin

    private val _navigationDestination = MutableLiveData<NavigationDestination>()
    val navigationDestination: LiveData<NavigationDestination>
        get() = _navigationDestination

    val memberList = mutableListOf<String>()
    private val groupList = mutableListOf<String>()

    init {
        if (Firebase.auth.currentUser?.uid != null) {
            getMemberIdList()
            _isLogin.value = true
        } else {
            _isLogin.value = false
        }
    }

    fun startSetAlarmForTodoList() {
        viewModelScope.launch {
            RebuildAlarm().updateNewTodoListToAlarmManager(firebaseDataRepository)
        }
    }

    fun loginSuccess() {
        _isLogin.value = true
    }

    private fun getMemberIdList() {
        viewModelScope.launch {
            val auth = Firebase.auth
            when (val user = firebaseDataRepository.getUser(auth.currentUser?.uid!!)) {
                is Result.Success -> {
                    user.data.groupList.forEach { groupId ->
                        groupList.add(groupId)
                        getMembers(groupId).forEach { memberId ->
                            memberList.add(memberId.userId ?: "")
                        }
                    }
                }
            }
        }
    }

    private suspend fun getMembers(id: String): List<Member> {
        return when (val data = firebaseDataRepository.getMembers(id)) {
            is Result.Success -> data.data
            else -> listOf()
        }
    }

    fun getNavigationDestination(navigationDestination: NavigationDestination) {
        _navigationDestination.value = navigationDestination
    }
}

enum class NavigationDestination(val title: String) {
    HomeFragment("toHealth"),
    MyGroupFragment("我的群組"),
    MyManageFragment("管理項目"),
    MyStatisticFragment("統計數據"),
    ItemEditFragment("新增項目"),
    GroupFragment("群組大廳"),
    EditNoteAndReminderFragment("編輯留言板"),
    MeasureRecordFragment("測量記錄"),
    CareRecordFragment("心情紀錄"),
    FastAddFragment("添加完成項目"),
    GroupMemberMenageFragment("管理群組成員項目"),
    GroupMemberStatisticFragment("查看群組成員統計數據"),
    ItemUpdateFragment("項目更新"),
    NotificationFragment("警告消息"),
    LoginFragment("登錄頁面"),
    OtherFragment("toHealth")
}
