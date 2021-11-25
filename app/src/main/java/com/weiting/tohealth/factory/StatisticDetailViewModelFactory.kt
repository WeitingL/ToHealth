package com.weiting.tohealth.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.mystatisticpage.StatisticDetailViewModel

class StatisticDetailViewModelFactory(
    private val firebaseDataRepository: FirebaseRepository,
    private val userId: String,
    private val itemType: ItemType
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(StatisticDetailViewModel::class.java) ->
                    StatisticDetailViewModel(firebaseDataRepository, userId, itemType)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
