package com.weiting.tohealth.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.mygrouppage.dialogs.AddGroupViewModel
import com.weiting.tohealth.mygrouppage.dialogs.HowToAddGroup

class AddGroupViewModelFactory(
    private val firebaseDataRepository: FirebaseRepository,
    private val howToAddGroup: HowToAddGroup
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(AddGroupViewModel::class.java) ->
                    AddGroupViewModel(firebaseDataRepository, howToAddGroup)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
