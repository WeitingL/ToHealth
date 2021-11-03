package com.weiting.tohealth.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.User
import com.weiting.tohealth.itemeditpage.ItemEditViewModel
import com.weiting.tohealth.mymanagepage.ManageDetailViewModel
import com.weiting.tohealth.mymanagepage.ManageType

class ItemEditViewModelFactory(
    private val firebaseDataRepository: FirebaseRepository,
    private val user: User
):ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ItemEditViewModel::class.java) ->
                    ItemEditViewModel(firebaseDataRepository, user)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}