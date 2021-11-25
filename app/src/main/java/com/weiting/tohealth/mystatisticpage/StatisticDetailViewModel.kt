package com.weiting.tohealth.mystatisticpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.mystatisticpage.activitychart.AnalyzeActivityLog
import com.weiting.tohealth.mystatisticpage.carechart.AnalyzeCareLog
import com.weiting.tohealth.mystatisticpage.drugchart.AnalyzeDrugLog
import com.weiting.tohealth.mystatisticpage.measurechart.AnalyzeMeasureLog
import kotlinx.coroutines.launch

class StatisticDetailViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val userId: String,
    statisticType: StatisticType
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _logList = MutableLiveData<List<LogItem>>()
    val logList: LiveData<List<LogItem>>
        get() = _logList

    private val logItemList = mutableListOf<LogItem>()

    init {
        when (statisticType) {
            StatisticType.DRUG -> getDrugLogs()
            StatisticType.CARE -> getCareLogs()
            StatisticType.ACTIVITY -> getActivityLogs()
            StatisticType.MEASURE -> getMeasureLogs()
        }
        _isLoading.value = true
    }

    fun loadingFinished() {
        _isLoading.value = false
    }

    private fun getMeasureLogs() {
        viewModelScope.launch {
            val measureList = firebaseDataRepository.getAllMeasures(userId)
            measureList.forEach {
                it.measureLogs = firebaseDataRepository.getMeasureRecord(it.id?:"")
                if (it.measureLogs.isNotEmpty()) {
                    logItemList.add(AnalyzeMeasureLog().revertToResultInDateList(it))
                }
            }
//            logItemList.add(LogItem.Bottom)
            _logList.value = logItemList
            loadingFinished()
        }
    }

    private fun getCareLogs() {
        viewModelScope.launch {
            val careList = firebaseDataRepository.getAllCares(userId)
            careList.forEach {
                it.careLogs = firebaseDataRepository.getCareRecord(it.id?:"")
                if (it.careLogs.isNotEmpty()) {
                    logItemList.add(AnalyzeCareLog().revertToResultInDateList(it))
                }
            }
//            logItemList.add(LogItem.Bottom)
            _logList.value = logItemList
            loadingFinished()
        }
    }

    private fun getActivityLogs() {
        viewModelScope.launch {
            val activityList = firebaseDataRepository.getAllActivities(userId)
            activityList.forEach {
                it.eventLogs = firebaseDataRepository.getActivityRecord(it.id?:"")
                if (it.eventLogs.isNotEmpty()) {
                    logItemList.add(AnalyzeActivityLog().revertToResultInDateList(it))
                }
            }
//            logItemList.add(LogItem.Bottom)
            _logList.value = logItemList
            loadingFinished()
        }
    }

    private fun getDrugLogs() {
        viewModelScope.launch {
            val drugList = firebaseDataRepository.getAllDrugs(userId)
            drugList.forEach {
                it.drugLogs = firebaseDataRepository.getDrugRecord(it.id?:"")
                if (it.drugLogs.isNotEmpty()) {
                    logItemList.add(AnalyzeDrugLog().revertToResultInDateList(it))
                }
            }
//            logItemList.add(LogItem.Bottom)
            _logList.value = logItemList
            loadingFinished()
        }
    }
}

sealed class LogItem {
    data class DrugLogItem(val itemName: String, val list: List<ResultInDate>) : LogItem()
    data class CareLogItem(val itemName: String, val list: List<ResultInDateForCare>) : LogItem()
    data class ActivityLogItem(val itemName: String, val list: List<ResultInDate>) : LogItem()
    data class MeasureLogItem(
        val itemName: String,
        val type: Int,
        val list: List<ResultInDateForMeasure>
    ) : LogItem()

    object Bottom : LogItem()
}

// DrugLogData and CareLogData
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
