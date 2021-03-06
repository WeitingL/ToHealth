package com.weiting.tohealth.itemeditpage

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.ItemUpdateFragmentBinding
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt
import com.weiting.tohealth.works.RebuildAlarm
import java.util.*
import kotlinx.coroutines.launch

class ItemUpdateViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val itemData: ItemData
) : ViewModel() {

    private val _periodType = MutableLiveData<Int>()
    val periodType: LiveData<Int>
        get() = _periodType

    private val _statusSelected = MutableLiveData<Int>()
    val statusSelected: LiveData<Int>
        get() = _statusSelected

    // TimeSet
    private val _timePointSet = MutableLiveData<MutableList<Timestamp>>()
    val timePointSet: LiveData<MutableList<Timestamp>>
        get() = _timePointSet

    private val timestampList = mutableListOf<Timestamp>()
    private val dateList = mutableListOf<Int>()

    fun getStatus(int: Int) {
        _statusSelected.value = int
    }

    fun removeTimeSet(position: Int) {
        dateList.removeAt(position)
        timestampList.removeAt(position)
        timestampList.sortBy {
            getTimeStampToTimeInt(it)
        }
        dateList.sort()
        _timePointSet.value = timestampList
    }

    fun getTimeSet(time: Long?) {
        val c = Calendar.getInstance()
        c.time = Date(time ?: 0)

        // Repeat check
        if ((c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE)) in dateList) {
            Toast.makeText(
                PublicApplication.application.applicationContext,
                "重複添加囉!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            dateList.add(c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE))
            timestampList.add(Timestamp(Date(time ?: 0)))

            timestampList.sortBy {
                getTimeStampToTimeInt(it)
            }
            dateList.sort()
//            timestampList.forEach {
//                Log.i("data?", "${ toTimeFromTimeStamp(it)} ${getTimeStampToTimeInt(it)} $dateList")
//            }
            _timePointSet.value = timestampList
        }
    }

    fun setPeriodType(int: Int) {
        _periodType.value = int
    }

    fun updateItem(binding: ItemUpdateFragmentBinding) {
        when (itemData.itemType) {
            ItemType.DRUG -> {
                val data = itemData.drugData ?: Drug()

                data.unit = binding.spUnitUpdate.selectedItemPosition
                data.dose = binding.etvDrugDoseUpdate.text.toString().toFloat()
                data.period = mapOf(
                    TYPE to binding.spPeriodUpdate.selectedItemPosition,
                    N to binding.spOngoingDayUpdate.selectedItemPosition,
                    X to binding.spSuspendDayUpdate.selectedItemPosition
                )
                data.executedTime = timePointSet.value ?: data.executedTime
                data.stock = binding.etvStockUpdate.text.toString().toFloat()
                data.editor = UserManager.UserInfo.id
                data.lastEditTime = Timestamp.now()
                data.status = statusSelected.value ?: data.status

                firebaseDataRepository.updateDrug(data)
            }

            ItemType.MEASURE -> {
                val data = itemData.measureData ?: Measure()

                data.lastEditTime = Timestamp.now()
                data.editor = UserManager.UserInfo.id
                data.executedTime = timePointSet.value ?: data.executedTime
                data.status = statusSelected.value ?: data.status

                firebaseDataRepository.updateMeasure(data)
            }

            ItemType.EVENT -> {
                val data = itemData.eventData ?: Event()

                data.period = mapOf(
                    TYPE to binding.spPeriodUpdate.selectedItemPosition,
                    N to binding.spOngoingDayUpdate.selectedItemPosition,
                    X to binding.spSuspendDayUpdate.selectedItemPosition
                )
                data.executedTime = timePointSet.value ?: data.executedTime
                data.editor = UserManager.UserInfo.id
                data.lastEditTime = Timestamp.now()
                data.status = statusSelected.value ?: data.status

                firebaseDataRepository.updateEvent(data)
            }

            ItemType.CARE -> {
                val data = itemData.careData ?: Care()

                data.period = mapOf(
                    TYPE to binding.spPeriodUpdate.selectedItemPosition,
                    N to binding.spOngoingDayUpdate.selectedItemPosition,
                    X to binding.spSuspendDayUpdate.selectedItemPosition
                )
                data.executedTime = timePointSet.value ?: data.executedTime
                data.editor = UserManager.UserInfo.id
                data.lastEditTime = Timestamp.now()
                data.status = statusSelected.value ?: data.status

                firebaseDataRepository.updateCare(data)
            }
        }
    }

    fun startSetAlarmForTodoList() {
        viewModelScope.launch {
            RebuildAlarm().updateNewTodoListToAlarmManager(firebaseDataRepository)
        }
    }
}
