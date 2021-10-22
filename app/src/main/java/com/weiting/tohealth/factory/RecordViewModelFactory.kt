package com.weiting.tohealth.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.homepage.recorddialog.RecordViewModel


class RecordViewModelFactory(
    private val firebaseDataRepository: FirebaseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(RecordViewModel::class.java) ->
                    RecordViewModel(firebaseDataRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

}