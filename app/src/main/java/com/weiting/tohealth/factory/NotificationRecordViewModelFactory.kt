package com.weiting.tohealth.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.alertmessagepage.AlertMessageViewModel

class NotificationRecordViewModelFactory(
    private val firebaseDataRepository: FirebaseRepository,
    private val memberList: List<String>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(AlertMessageViewModel::class.java) ->
                    AlertMessageViewModel(firebaseDataRepository, memberList)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
