package com.weiting.tohealth.itemeditpage

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.ItemEditFragmentBinding
import com.weiting.tohealth.toDateFromMilliTime
import java.util.*

class ItemEditViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val user: User
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

    private val _timePointSet = MutableLiveData<MutableList<Timestamp>>()
    val timePointSet: LiveData<MutableList<Timestamp>>
        get() = _timePointSet

    private val timestampList = mutableListOf<Timestamp>()

    //To check the double time set: 13:00 -> 1300
    private val timeList = mutableListOf<Int>()

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
        c.time = Date(time ?: 0)

        if ((c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE)) in timeList) {
            Toast.makeText(
                PublicApplication.application.applicationContext,
                "重複添加囉!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            timeList.add(c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE))
            timestampList.add(Timestamp(Date(time ?: 0)))
            _timePointSet.value = timestampList
        }

    }

    fun removeTimeSet(position: Int) {
        timeList.removeAt(position)
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
                    userId = user.id,
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
                    lastEditTime = Timestamp.now(),
                    stock = Integer.parseInt(binding.etvStock.text.toString()),
                    editor = UserManager.userId,
                    createTime = Timestamp.now(),
                    status = 0
                )
                firebaseDataRepository.postDrug(data)
            }
            ItemType.MEASURE -> {
                val data = Measure(
                    userId = user.id,
                    type = binding.spItemName.selectedItemPosition,
                    executeTime = timePointSet.value!!,
                    editor = UserManager.userId,
                    createTime = Timestamp.now(),
                    lastEditTime = Timestamp.now(),
                    status = 0
                )

                firebaseDataRepository.postMeasure(data)
            }
            ItemType.ACTIVITY -> {
                val data = Activity(
                    userId = user.id,
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
                    lastEditTime = Timestamp.now(),
                    status = 0
                )

                firebaseDataRepository.postActivity(data)
            }
            ItemType.CARE -> {
                val data = Care(
                    userId = user.id,
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
                    lastEditTime = Timestamp.now(),
                    status = 0
                )

                firebaseDataRepository.postCare(data)
            }
            else -> {
            }
        }
    }

}