package com.weiting.tohealth.itemeditpage

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.ItemEditFragmentBinding
import com.weiting.tohealth.toDateFromMilliTime
import com.weiting.tohealth.toTimeFromMilliTime
import java.sql.Time
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

    private val _currentPeriodType = MutableLiveData<Int>()
    val currentPeriodType: LiveData<Int>
        get() = _currentPeriodType

    private val _dateSet = MutableLiveData<String>()
    val dateSet: LiveData<String>
        get() = _dateSet

    private val _dateSetInLong = MutableLiveData<Long>()

    private val _timePointSet = MutableLiveData<MutableList<Timestamp>>()
    val timePointSet: LiveData<MutableList<Timestamp>>
        get() = _timePointSet

    private val timestampList = mutableListOf<Timestamp>()
    //To check the double time set: 13:00 -> 1300
    private val dateList = mutableListOf<Int>()

    private var startDateInLong = 0L

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

    fun getTimeSet(time: Long?) {
        val c = Calendar.getInstance()
        c.time = Date(time?:0)

        if ((c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE)) in dateList){
            Toast.makeText(
                PublicApplication.application.applicationContext,
                "重複添加囉!",
                Toast.LENGTH_LONG
            ).show()
        }else{
            dateList.add(c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE))
            timestampList.add(Timestamp(Date(time ?: 0)))
            _timePointSet.value = timestampList
        }

    }

    fun removeTimeSet(position: Int){
        dateList.removeAt(position)
        timestampList.removeAt(position)
        _timePointSet.value = timestampList
    }

    fun getDateSet(time: Long?) {
        startDateInLong = time ?: 0
        _dateSet.value = toDateFromMilliTime(time ?: 0)
    }

    fun postData(binding: ItemEditFragmentBinding) {
        when (editItemType.value) {
            ItemType.DRUG -> {
                val data = Drug(
                    userId = UserManager.userId,
                    drugName = binding.tilDrugName.editText?.text.toString(),
                    dose = Integer.parseInt(binding.etvDrugDose.text.toString()),
                    unit = binding.spUnit.selectedItemPosition,
                    endDate = mapOf(
                        "type" to endDateSelected.value,
                        "day" to binding.spEndDate.selectedItemPosition
                    ),
                    startDate = Timestamp(Date(startDateInLong)),
                    period = mapOf(
                        "type" to binding.spPeriod.selectedItemPosition,
                        "N" to binding.spOngoingDay.selectedItemPosition,
                        "X" to binding.spSuspendDay.selectedItemPosition
                    ),
                    executeTime = timePointSet.value!!,
                    stock = Integer.parseInt(binding.etvStock.text.toString()),
                    editor = UserManager.userId,
                    createTime = Timestamp.now(),
                    status = editType.value
                )
                firebaseDataRepository.postDrug(data)
            }
            ItemType.MEASURE -> {
                val data = Measure(
                    userId = UserManager.userId,
                    type = binding.spItemName.selectedItemPosition,
                    executeTime = timePointSet.value!!,
                    editor = UserManager.userId,
                    createTime = Timestamp.now(),
                    status = editType.value
                )

                firebaseDataRepository.postMeasure(data)
            }
            ItemType.ACTIVITY -> {
                val data = Activity(
                    userId = UserManager.userId,
                    type = binding.spItemName.selectedItemPosition,
                    endDate = mapOf(
                        "type" to endDateSelected.value,
                        "day" to binding.spEndDate.selectedItemPosition
                    ),
                    startDate = Timestamp(Date(startDateInLong)),
                    period = mapOf(
                        "type" to binding.spPeriod.selectedItemPosition,
                        "N" to binding.spOngoingDay.selectedItemPosition,
                        "X" to binding.spSuspendDay.selectedItemPosition,
                    ),
                    executeTime = timePointSet.value!!,
                    editor = UserManager.userId,
                    createTime = Timestamp.now(),
                    status = editType.value
                )

                firebaseDataRepository.postActivity(data)
            }
            ItemType.CARE -> {
                val data = Care(
                    userId = UserManager.userId,
                    type = binding.spItemName.selectedItemPosition,
                    endDate = mapOf(
                        "type" to endDateSelected.value,
                        "day" to binding.spEndDate.selectedItemPosition
                    ),
                    startDate = Timestamp(Date(startDateInLong)),
                    period = mapOf(
                        "type" to binding.spPeriod.selectedItemPosition,
                        "N" to binding.spOngoingDay.selectedItemPosition,
                        "X" to binding.spSuspendDay.selectedItemPosition,
                    ),
                    executeTime = timePointSet.value!!,
                    editor = UserManager.userId,
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