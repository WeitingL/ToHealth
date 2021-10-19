package com.weiting.tohealth.itemeditpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.ItemType

enum class EditType {
    CREATE, UPDATE, FINISHED
}

class ItemEditViewModel : ViewModel() {

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

    fun getCurrentPeriodType(int: Int){
        _currentPeriodType.value = int
    }


}