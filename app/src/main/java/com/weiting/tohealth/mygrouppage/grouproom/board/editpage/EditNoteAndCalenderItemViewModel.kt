package com.weiting.tohealth.mygrouppage.grouproom.board.editpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.Reminder
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.data.Note
import com.weiting.tohealth.util.Util.toDateAndTimeFromMilliTime

enum class EditBoardType {
    NOTE, REMINDER
}

class EditNoteAndCalenderItemViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val group: Group
) : ViewModel() {

    private val _editBoardType = MutableLiveData<EditBoardType>()
    val editBoardType: LiveData<EditBoardType>
        get() = _editBoardType

    private val _timeSet = MutableLiveData<String>()
    val timeSet: LiveData<String>
        get() = _timeSet

    val longTime = MutableLiveData<Long>()

    init {
        _editBoardType.value = EditBoardType.NOTE
    }

    fun getEditBoardType(int: Int) {
        when (int) {
            0 -> _editBoardType.value = EditBoardType.NOTE
            1 -> _editBoardType.value = EditBoardType.REMINDER
        }
    }

    fun getTimeSet(time: Long?) {
        _timeSet.value = toDateAndTimeFromMilliTime(time ?: 0)
        longTime.value = time ?: 0
    }

    fun postNote(note: Note) {
        firebaseDataRepository.postNote(note, group.id ?: "")
    }

    fun postCalenderItem(reminder: Reminder) {
        firebaseDataRepository.postCalenderItem(reminder, group.id ?: "")
    }
}
