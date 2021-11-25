package com.weiting.tohealth.mygrouppage.grouproom.board

import androidx.lifecycle.*
import com.weiting.tohealth.data.Reminder
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.data.Note
import kotlinx.coroutines.launch

class BoardViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val group: Group
) : ViewModel() {

    private val _boardList = MutableLiveData<List<BoardType>>()
    val boardList: LiveData<List<BoardType>>
        get() = _boardList

    private val notesLiveList = firebaseDataRepository.getLiveNotes(group.id ?: "")
    private val calenderLiveItem = firebaseDataRepository.getLiveReminders(group.id ?: "")

    private val noteCurrentList = mutableListOf<Note>()
    private val calenderCurrentList = mutableListOf<Reminder>()

    val boardLiveData = MediatorLiveData<MutableList<BoardType>>().apply {

        addSource(notesLiveList) { noteList ->
            viewModelScope.launch {
                noteCurrentList.clear()
                noteList.forEach { note ->
                    note.editor = firebaseDataRepository.getUser(note.editor ?: "").name
                    noteCurrentList.add(note)
                }

                value = getBoards()
            }
        }

        addSource(calenderLiveItem) { calenderList ->
            viewModelScope.launch {
                calenderCurrentList.clear()
                calenderList.forEach { calenderItem ->
                    calenderItem.editor =
                        firebaseDataRepository.getUser(calenderItem.editor ?: "").name
                    calenderCurrentList.add(calenderItem)
                }
                value = getBoards()
            }
        }
    }

    private fun getBoards(): MutableList<BoardType> {

        return when {
            noteCurrentList.isNotEmpty() && calenderCurrentList.isNotEmpty() -> {
                mutableListOf(
                    BoardType.Notes(noteCurrentList),
                    BoardType.CalenderItems(calenderCurrentList)
                )
            }
            noteCurrentList.isNotEmpty() -> {
                mutableListOf(
                    BoardType.Notes(noteCurrentList)
                )
            }
            calenderCurrentList.isNotEmpty() -> {
                mutableListOf(

                    BoardType.CalenderItems(calenderCurrentList)
                )
            }
            else -> {
                mutableListOf()
            }
        }
    }

    fun deleteNote(note: Note) {
        firebaseDataRepository.deleteNote(note, group.id ?: "")
    }

    fun deleteReminder(reminder: Reminder) {
        firebaseDataRepository.deleteReminder(reminder, group.id ?: "")
    }
}

sealed class BoardType {

    data class Notes(val list: List<Note>) : BoardType()

    data class CalenderItems(val list: List<Reminder>) : BoardType()
}
