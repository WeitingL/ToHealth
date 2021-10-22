package com.weiting.tohealth.itemeditpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.ItemEditFragmentBinding
import com.weiting.tohealth.toDateFromMilliTime
import com.weiting.tohealth.toTimeFromMilliTime
import java.util.*

enum class EditType(val value: Int) {
    CREATE(0), UPDATE(1), FINISHED(2)
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

    private val _timeSetInLong = MutableLiveData<Long>()
    val timeSetInLong: LiveData<Long>
        get() = _timeSetInLong

    private val _dateSet = MutableLiveData<String>()
    val dateSet: LiveData<String>
        get() = _dateSet

    private val _dateSetInLong = MutableLiveData<Long>()
    val dateSetInLong: LiveData<Long>
        get() = _dateSetInLong

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

    fun getcurrentPeriodSubType(int: Int) {
        _currentPeriodSubType.value = int
    }

    fun getTimeSet(time: Long?) {
        _timeSetInLong.value = time ?: 0
        _timeSet.value = toTimeFromMilliTime(time ?: 0)
    }

    fun getDateSet(time: Long?) {
        _dateSetInLong.value = time ?: 0
        _dateSet.value = toDateFromMilliTime(time ?: 0)
    }

    fun postData(binding: ItemEditFragmentBinding) {
        when (editItemType.value) {
            ItemType.DRUG -> {
                val data = Drug(
                    userId = "Weiting",
                    drugName = binding.tilDrugName.editText?.text.toString(),
                    dose = Integer.parseInt(binding.etvStock.text.toString()),
                    unit = binding.spUnit.selectedItemPosition,
                    endDate = mapOf(
                        "type" to endDateSelected.value,
                        "day" to binding.spEndDate.selectedItemPosition
                    ),
                    startDate = Timestamp(Date(timeSetInLong.value!!)),
                    period = mapOf(
                        "type" to binding.spPeriod.selectedItemPosition,
                        "N" to binding.spOngoingUnit.selectedItemPosition,
                        "X" to binding.spSuspendDay.selectedItemPosition,
                        "Y" to binding.spCycle.selectedItemPosition,
                        "subType" to binding.spSubtype.selectedItemPosition,
                        "Z" to binding.spSubOngoningUnit.selectedItemPosition
                    ),
                    firstTimePerDay = Timestamp(Date(timeSetInLong.value!!)),
                    stock = Integer.parseInt(binding.etvStock.text.toString()),
                    editor ="Test",
                    createTime = Timestamp.now(),
                    status = editType.value
                )
                firebaseDataRepository.postDrug(data)
            }
            ItemType.MEASURE -> {
                val data = Measure(
                    userId = "Weiting",
                    type = binding.spItemName.selectedItemPosition,
                    firstTimePerDay = Timestamp(Date(timeSetInLong.value!!)),
                    editor ="Test",
                    createTime = Timestamp.now(),
                    status = editType.value
                )

                firebaseDataRepository.postMeasure(data)
            }
            ItemType.ACTIVITY -> {
                val data = Activity(
                    userId = "Weiting",
                    type = binding.spItemType.selectedItemPosition,
                    endDate = mapOf(
                        "type" to endDateSelected.value,
                        "day" to binding.spEndDate.selectedItemPosition
                    ),
                    startDate = Timestamp(Date(timeSetInLong.value!!)),
                    period = mapOf(
                        "type" to binding.spPeriod.selectedItemPosition,
                        "N" to binding.spOngoingUnit.selectedItemPosition,
                        "X" to binding.spSuspendDay.selectedItemPosition,
                        "Y" to binding.spCycle.selectedItemPosition,
                        "subType" to binding.spSubtype.selectedItemPosition,
                        "Z" to binding.spSubOngoningUnit.selectedItemPosition
                    ),
                    firstTimePerDay = Timestamp(Date(timeSetInLong.value!!)),
                    editor ="Test",
                    createTime = Timestamp.now(),
                    status = editType.value
                )

                firebaseDataRepository.postActivity(data)
            }
            ItemType.CARE -> {
                val data = Care(
                    userId = "Weiting",
                    type = binding.spItemType.selectedItemPosition,
                    endDate = mapOf(
                        "type" to endDateSelected.value,
                        "day" to binding.spEndDate.selectedItemPosition
                    ),
                    startDate = Timestamp(Date(timeSetInLong.value!!)),
                    period = mapOf(
                        "type" to binding.spPeriod.selectedItemPosition,
                        "N" to binding.spOngoingUnit.selectedItemPosition,
                        "X" to binding.spSuspendDay.selectedItemPosition,
                        "Y" to binding.spCycle.selectedItemPosition,
                        "subType" to binding.spSubtype.selectedItemPosition,
                        "Z" to binding.spSubOngoningUnit.selectedItemPosition
                    ),
                    firstTimePerDay = Timestamp(Date(timeSetInLong.value!!)),
                    editor ="Test",
                    createTime = Timestamp.now(),
                    status = editType.value
                )

                firebaseDataRepository.postCare(data)
            }
            else -> {
            }
        }
    }

}