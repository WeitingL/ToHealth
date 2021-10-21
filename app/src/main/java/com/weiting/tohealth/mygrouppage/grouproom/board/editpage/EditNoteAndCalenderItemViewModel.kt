package com.weiting.tohealth.mygrouppage.grouproom.board.editpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.toDateAndTimeFromMilliTime

enum class EditBoardType {
    NOTE, REMINDER
}

class EditNoteAndCalenderItemViewModel : ViewModel() {

    private val _editBoardType = MutableLiveData<EditBoardType>()
    val editBoardType: LiveData<EditBoardType>
        get() = _editBoardType

    private val _timeSet = MutableLiveData<String>()
    val timeSet: LiveData<String>
        get() = _timeSet

    init {
        _editBoardType.value = EditBoardType.NOTE
    }

    fun getEditBoardType(int: Int){
        when(int){
            0 -> _editBoardType.value = EditBoardType.NOTE
            1 -> _editBoardType.value = EditBoardType.REMINDER
        }
    }

    fun getTimeSet(time: Long?) {
        _timeSet.value = toDateAndTimeFromMilliTime(time?:0)
    }

}