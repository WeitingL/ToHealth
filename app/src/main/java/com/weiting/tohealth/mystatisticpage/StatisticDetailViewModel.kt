package com.weiting.tohealth.mystatisticpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Drug
import com.weiting.tohealth.data.DrugLog
import com.weiting.tohealth.data.FirebaseDataRepository
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.mystatisticpage.activitychart.AnalyzeActivityLog
import com.weiting.tohealth.mystatisticpage.drugchart.AnalyzeDrugLog
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.*

class StatisticDetailViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val userId: String,
    private val statisticType: StatisticType
) : ViewModel() {

    private val _logList = MutableLiveData<List<LogItem>>()
    val logList: LiveData<List<LogItem>>
        get() = _logList

    private val logItemList = mutableListOf<LogItem>()

    init {
        when (statisticType) {
            StatisticType.DRUG -> getDrugLogs()
            StatisticType.CARE -> getCareLogs()
            StatisticType.ACTIVITY -> getActivityLogs()
            StatisticType.MEASURE -> {

            }
        }


    }

    private fun getCareLogs(){
        _logList.value = listOf(LogItem.CareLogItem("Care", listOf()), LogItem.Bottom)
    }


    private fun getActivityLogs() {
        viewModelScope.launch {
            val activityList = firebaseDataRepository.getAllActivities(userId)
            activityList.forEach {
                it.activityLogs = firebaseDataRepository.getActivityRecord(it.id!!, Timestamp.now())
                logItemList.add(AnalyzeActivityLog().revertToResultInDateList(it))
            }
            logItemList.add(LogItem.Bottom)
            _logList.value = logItemList
        }
    }

    private fun getDrugLogs() {
        viewModelScope.launch {
            val drugList = firebaseDataRepository.getAllDrugs(userId)
            drugList.forEach {
                it.drugLogs = firebaseDataRepository.getDrugRecord(it.id!!, Timestamp.now())
                logItemList.add(AnalyzeDrugLog().revertToResultInDateList(it))
            }
            logItemList.add(LogItem.Bottom)
            _logList.value = logItemList
        }
    }
}

sealed class LogItem {
    data class DrugLogItem(val itemName: String, val list: List<ResultInDate>) : LogItem()
    data class CareLogItem(val itemName: String, val list: List<ResultInDate>) : LogItem()
    data class ActivityLogItem(val itemName: String, val list: List<ResultInDate>): LogItem()
    object Bottom : LogItem()
}

//DrugLogData and CareLogData
data class ResultInDate(
    val date: Timestamp,
    val results: List<Int>
)

