package com.weiting.tohealth.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.mygrouppage.grouproom.board.BoardViewModel
import com.weiting.tohealth.mymanagepage.ManageDetailViewModel
import com.weiting.tohealth.mymanagepage.ManageType

class BoardViewModelFactory(
    private val firebaseDataRepository: FirebaseRepository,
    private val group: Group
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(BoardViewModel::class.java) ->
                    BoardViewModel(firebaseDataRepository, group)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}