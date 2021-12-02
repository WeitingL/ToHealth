package com.weiting.tohealth.mystatisticpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.data.Result
import com.weiting.tohealth.mystatisticpage.eventchart.AnalyzeActivityLog
import com.weiting.tohealth.mystatisticpage.carechart.AnalyzeCareLog
import com.weiting.tohealth.mystatisticpage.drugchart.AnalyzeDrugLog
import com.weiting.tohealth.mystatisticpage.measurechart.AnalyzeMeasureLog
import kotlinx.coroutines.launch

class StatisticDetailViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val userId: String,
    itemType: ItemType
) : ViewModel() {

    private val _logList = MutableLiveData<List<LogItem>>()
    val logList: LiveData<List<LogItem>>
        get() = _logList

    private val logItemList = mutableListOf<LogItem>()

    init {
        when (itemType) {
            ItemType.DRUG -> getDrugLogs()
            ItemType.CARE -> getCareLogs()
            ItemType.EVENT -> getEventLogs()
            ItemType.MEASURE -> getMeasureLogs()
        }
    }

    private fun getMeasureLogs() {
        viewModelScope.launch {
            val measureList = when(val result = firebaseDataRepository.getAllMeasures(userId)){
                is Result.Success -> result.data
                else -> listOf()
            }
            measureList.forEach {
                it.measureLogs = when(val result = firebaseDataRepository.getMeasureLogs(it.id?:"")){
                    is Result.Success -> result.data
                    else -> listOf()
                }
                if (it.measureLogs.isNotEmpty()) {
                    logItemList.add(AnalyzeMeasureLog().revertToResultInDateList(it))
                }
            }
            _logList.value = logItemList
        }
    }

    private fun getCareLogs() {
        viewModelScope.launch {
            val careList = when(val result = firebaseDataRepository.getAllCares(userId)){
                is Result.Success -> result.data
                else -> listOf()
            }
            careList.forEach {
                it.careLogs = when(val result = firebaseDataRepository.getCareLogs(it.id?:"")){
                    is Result.Success -> result.data
                    else -> listOf()
                }
                if (it.careLogs.isNotEmpty()) {
                    logItemList.add(AnalyzeCareLog().revertToResultInDateList(it))
                }
            }
            _logList.value = logItemList
        }
    }

    private fun getEventLogs() {
        viewModelScope.launch {
            val events = when(val result = firebaseDataRepository.getAllEvents(userId)){
                is Result.Success -> result.data
                else -> listOf()
            }

            events.forEach {
                it.eventLogs = when(val result = firebaseDataRepository.getEventLogs(it.id?:"")){
                    is Result.Success -> result.data
                    else -> listOf()
                }
                if (it.eventLogs.isNotEmpty()) {
                    logItemList.add(AnalyzeActivityLog().revertToResultInDateList(it))
                }
            }
            _logList.value = logItemList
        }
    }

    private fun getDrugLogs() {
        viewModelScope.launch {
            val drugList = when(val result = firebaseDataRepository.getAllDrugs(userId)){
                is Result.Success -> result.data
                else -> listOf()
            }
            drugList.forEach {
                it.drugLogs = when(val result = firebaseDataRepository.getDrugLogs(it.id?:"")){
                    is Result.Success -> result.data
                    else -> listOf()
                }
                if (it.drugLogs.isNotEmpty()) {
                    logItemList.add(AnalyzeDrugLog().revertToResultInDateList(it))
                }
            }
            _logList.value = logItemList
        }
    }
}

sealed class LogItem {
    data class DrugLogItem(val itemName: String, val list: List<ResultInDate>) : LogItem()
    data class CareLogItem(val itemName: String, val list: List<ResultInDateForCare>) : LogItem()
    data class EventLogItem(val itemName: String, val list: List<ResultInDate>) : LogItem()
    data class MeasureLogItem(
        val itemName: String,
        val type: Int,
        val list: List<ResultInDateForMeasure>
    ) : LogItem()

    object Bottom : LogItem()
}

// DrugLogData and EventLogData
data class ResultInDate(
    val date: Timestamp,
    val results: List<Map<String, String>>
)

data class ResultInDateForCare(
    val createTime: Timestamp,
    val emotion: Int?,
    val note: String
)

data class ResultInDateForMeasure(
    val createTime: Timestamp,
    val results: Int?,
    val record: Map<String, Int?>
)
