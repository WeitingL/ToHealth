package com.weiting.tohealth.mygrouppage.grouproom.board.editpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class EditBoardType {
    NOTE, REMINDER
}

class EditNoteAndCalenderItemViewModel : ViewModel() {

    private val _editBoardType = MutableLiveData<EditBoardType>()
    val editBoardType: LiveData<EditBoardType>
        get() = _editBoardType

    init {
        _editBoardType.value = EditBoardType.NOTE
    }

    fun getEditBoardType(int: Int){
        when(int){
            0 -> _editBoardType.value = EditBoardType.NOTE
            1 -> _editBoardType.value = EditBoardType.REMINDER
        }
    }

}