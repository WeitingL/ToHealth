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

    val notesList = firebaseDataRepository.getLiveNote(group.id!!)
    val calenderItem = firebaseDataRepository.getLiveCalenderItem(group.id!!)

    fun getCalenderList(list: List<CalenderItem>){

        _boardList.value = listOf()

        if (list.isNotEmpty() && !notesList.value.isNullOrEmpty()){
            val newList = listOf(BoardType.Notes(notesList.value!!),BoardType.CalenderItems(list) )
            _boardList.value = _boardList.value?.plus(newList)
        }else if (list.isNotEmpty() && notesList.value.isNullOrEmpty()){
            val newList = listOf(BoardType.CalenderItems(list))
            _boardList.value = _boardList.value?.plus(newList)
        }
    }

    fun getNotesList(list: List<Note>){

        _boardList.value = listOf()

        if (list.isNotEmpty() && !calenderItem.value.isNullOrEmpty()){
            val newList = listOf(BoardType.Notes(list),BoardType.CalenderItems(calenderItem.value!!) )
            _boardList.value = _boardList.value?.plus(newList)
        }else if (list.isNotEmpty() && calenderItem.value.isNullOrEmpty()){
            val newList = listOf(BoardType.Notes(list))
            _boardList.value = _boardList.value?.plus(newList)
        }
    }

}

sealed class BoardType {

    data class Notes(val list: List<Note>) : BoardType()

    data class CalenderItems(val list: List<CalenderItem>) : BoardType()

}