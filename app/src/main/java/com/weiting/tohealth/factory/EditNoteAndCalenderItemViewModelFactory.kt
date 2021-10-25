package com.weiting.tohealth.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.mygrouppage.MyGroupViewModel
import com.weiting.tohealth.mygrouppage.grouproom.board.editpage.EditNoteAndCalenderItemViewModel
import com.weiting.tohealth.mygrouppage.grouproom.members.MembersViewModel

class EditNoteAndCalenderItemViewModelFactory(
    private val firebaseDataRepository: FirebaseRepository,
    private val group: Group
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(EditNoteAndCalenderItemViewModel::class.java) ->
                    EditNoteAndCalenderItemViewModel(firebaseDataRepository, group)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

}