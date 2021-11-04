package com.weiting.tohealth.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.mygrouppage.MyGroupViewModel
import com.weiting.tohealth.mystatisticpage.StatisticDetailViewModel
import com.weiting.tohealth.mystatisticpage.StatisticType

class StatisticDetailViewModelFactory(
    private val firebaseDataRepository: FirebaseRepository,
    private val userId: String,
    private val statisticType: StatisticType
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(StatisticDetailViewModel::class.java) ->
                    StatisticDetailViewModel(firebaseDataRepository, userId, statisticType)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

}