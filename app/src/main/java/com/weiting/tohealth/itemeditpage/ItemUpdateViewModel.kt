package com.weiting.tohealth.itemeditpage

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.ItemUpdateFragmentBinding
import com.weiting.tohealth.mymanagepage.ManageType
import java.util.*

class ItemUpdateViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val itemData: ItemData,
    private val manageType: ManageType,
    private val user: User
) : ViewModel() {

    private val _periodType = MutableLiveData<Int>()
    val periodType: LiveData<Int>
        get() = _periodType

    private val _statusSelected = MutableLiveData<Int>()
    val statusSelected: LiveData<Int>
        get() = _statusSelected

    //TimeSet
    private val _timePointSet = MutableLiveData<MutableList<Timestamp>>()
    val timePointSet: LiveData<MutableList<Timestamp>>
        get() = _timePointSet

    private val timestampList = mutableListOf<Timestamp>()
    private val dateList = mutableListOf<Int>()

    fun getStatus(int: Int) {
        _statusSelected.value = int
    }

    //Time Set
    fun removeTimeSet(position: Int) {
        dateList.removeAt(position)
        timestampList.removeAt(position)
        _timePointSet.value = timestampList
    }

    fun getTimeSet(time: Long?) {
        val c = Calendar.getInstance()
        c.time = Date(time ?: 0)

        if ((c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE)) in dateList) {
            Toast.makeText(
                PublicApplication.application.applicationContext,
                "重複添加囉!",
                Toast.LENGTH_LONG
            ).show()

        } else {
            dateList.add(c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE))
            timestampList.add(Timestamp(Date(time ?: 0)))
            _timePointSet.value = timestampList
        }
    }

    fun setPeriodType(int: Int) {
        _periodType.value = int
    }

    fun updateItem(binding: ItemUpdateFragmentBinding) {
        when (manageType) {
            ManageType.DRUG -> {
                val data = itemData.DrugData!!

                data.unit = binding.spUnitUpdate.selectedItemPosition
                data.dose = Integer.parseInt(binding.etvDrugDoseUpdate.text.toString())
                data.period = mapOf(
                    "type" to binding.spPeriodUpdate.selectedItemPosition,
                    "N" to binding.spOngoingDayUpdate.selectedItemPosition,
                    "X" to binding.spSuspendDayUpdate.selectedItemPosition
                )
                data.executedTime = timePointSet.value?: data.executedTime
                data.stock = Integer.parseInt(binding.etvStockUpdate.text.toString())
                data.editor = UserManager.UserInformation.id
                data.lastEditTime = Timestamp.now()
                data.status = statusSelected.value ?: data.status

                firebaseDataRepository.updateDrug(data)
            }

            ManageType.MEASURE -> {
                val data = itemData.MeasureData!!

                data.lastEditTime = Timestamp.now()
                data.editor = UserManager.UserInformation.id
                data.executedTime = timePointSet.value?: data.executedTime
                data.status = statusSelected.value ?: data.status

                firebaseDataRepository.updateMeasure(data)
            }

            ManageType.ACTIVITY -> {
                val data = itemData.ActivityData!!

                data.period = mapOf(
                    "type" to binding.spPeriodUpdate.selectedItemPosition,
                    "N" to binding.spOngoingDayUpdate.selectedItemPosition,
                    "X" to binding.spSuspendDayUpdate.selectedItemPosition
                )
                data.executedTime = timePointSet.value?: data.executedTime
                data.editor = UserManager.UserInformation.id
                data.lastEditTime = Timestamp.now()
                data.status = statusSelected.value ?: data.status

                firebaseDataRepository.updateActivity(data)
            }

            ManageType.CARE -> {
                val data = itemData.CareData!!

                data.period = mapOf(
                    "type" to binding.spPeriodUpdate.selectedItemPosition,
                    "N" to binding.spOngoingDayUpdate.selectedItemPosition,
                    "X" to binding.spSuspendDayUpdate.selectedItemPosition
                )
                data.executeTime = timePointSet.value?: data.executeTime
                data.editor = UserManager.UserInformation.id
                data.lastEditTime = Timestamp.now()
                data.status = statusSelected.value ?: data.status

                firebaseDataRepository.updateCare(data)
            }
        }


    }


}