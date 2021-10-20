package com.weiting.tohealth.mygrouppage.grouproom.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.CalenderItem
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.data.Note

class BoardViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val group: Group
) : ViewModel() {

    private val _boardList = MutableLiveData<List<BoardType>>()
    val boardList: LiveData<List<BoardType>>
        get() = _boardList

    val list = listOf<BoardType>(
        BoardType.Notes(group.notes),
        BoardType.CalenderItems(group.calenderItems)
    )

    init {
        _boardList.value = list
    }

}

sealed class BoardType {

    data class Notes(val list: List<Note>) : BoardType()

    data class CalenderItems(val list: List<CalenderItem>) : BoardType()

}