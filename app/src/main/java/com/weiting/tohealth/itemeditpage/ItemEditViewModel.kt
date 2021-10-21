package com.weiting.tohealth.itemeditpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.mymanagepage.ManageType
import com.weiting.tohealth.toDateFromMilliTime
import com.weiting.tohealth.toTimeFromMilliTime

enum class EditType {
    CREATE, UPDATE, FINISHED
}

class ItemEditViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val editType: EditType
) : ViewModel() {

    private val _editItemType = MutableLiveData<ItemType>()
    val editItemType: LiveData<ItemType>
        get() = _editItemType

    private val _endDateSelected = MutableLiveData<Int>()
    val endDateSelected: LiveData<Int>
        get() = _endDateSelected

    private val _currentEditType = MutableLiveData<EditType>()
    val currentEditType: LiveData<EditType>
        get() = _currentEditType

    private val _currentPeriodType = MutableLiveData<Int>()
    val currentPeriodType: LiveData<Int>
        get() = _currentPeriodType

    private val _currentPeriodSubType = MutableLiveData<Int>()
    val currentPeriodSubType: LiveData<Int>
        get() = _currentPeriodSubType

    private val _timeSet = MutableLiveData<String>()
    val timeSet: LiveData<String>
        get() = _timeSet

    private val _dateSet = MutableLiveData<String>()
    val dateSet : LiveData<String>
        get() = _dateSet

    fun getSelectedItemType(int: Int) {
        when (int) {
            0 -> _editItemType.value = ItemType.DRUG
            1 -> _editItemType.value = ItemType.MEASURE
            2 -> _editItemType.value = ItemType.ACTIVITY
            3 -> _editItemType.value = ItemType.CARE
        }
    }

    fun getEndDateSelectedType(int: Int) {
        _endDateSelected.value = int
    }

    fun getCurrentPeriodType(int: Int) {
        _currentPeriodType.value = int
    }

    fun getcurrentPeriodSubType(int: Int){
        _currentPeriodSubType.value = int
    }

    fun getTimeSet(time: Long?) {
        _timeSet.value = toTimeFromMilliTime(time ?: 0)
    }

    fun getDateSet(time: Long?) {
        _dateSet .value = toDateFromMilliTime(time ?: 0)
    }

}