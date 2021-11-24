package com.weiting.tohealth.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.User
import com.weiting.tohealth.mymanagepage.ManageDetailViewModel
import com.weiting.tohealth.mymanagepage.ManageType

class ManageDetailViewModelFactory(
    private val firebaseDataRepository: FirebaseRepository,
    private val manageType: ManageType,
    private val user: User
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ManageDetailViewModel::class.java) ->
                    ManageDetailViewModel(firebaseDataRepository, user)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
