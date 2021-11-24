package com.weiting.tohealth.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.data.User
import com.weiting.tohealth.itemeditpage.ItemUpdateViewModel
import com.weiting.tohealth.mymanagepage.ManageType

class ItemUpdateViewModelFactory(
    private val firebaseDataRepository: FirebaseRepository,
    private val itemData: ItemData,
    private val manageType: ManageType,
    private val user: User
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ItemUpdateViewModel::class.java) ->
                    ItemUpdateViewModel(firebaseDataRepository, itemData, manageType)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
