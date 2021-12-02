package com.weiting.tohealth.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.*
import com.weiting.tohealth.util.AlterMessageGenerator
import kotlinx.coroutines.launch

class RecordViewModel(private val firebaseDataRepository: FirebaseRepository) :
    ViewModel(),
    AlterMessageGenerator {

    var careScore: Int? = null
    var careInfo: String? = null

    fun getCareScore(int: Int) {
        careScore = int
    }

    fun getCareInfo(note: String) {
        careInfo = note
    }

    fun postCareLog(itemId: String, careLog: CareLog) {
        careLog.record = mapOf(EMOTION to careScore?.toString(), NOTE to careInfo)
        firebaseDataRepository.postCareLog(itemId, careLog)
    }

    fun postMeasureLog(itemId: String, measureLog: MeasureLog, measureType: Int) {
        viewModelScope.launch {
            measureLog.id = when(val result = firebaseDataRepository.getMeasureLogId(itemId)){
                is Result.Success -> result.data
                else -> null
            }

            when (measureType) {
                0 -> {
                    if (isBloodPressureAbnormal(measureLog)) {
                        postAlterMessage(itemId, measureLog)
                    }
                }
                1 -> {
                    if (isBeforeMealBloodSugarAbnormal(measureLog)) {
                        postAlterMessage(itemId, measureLog)
                    }
                }
                2 -> {
                    if (isAfterMealBloodSugarAbnormal(measureLog)) {
                        postAlterMessage(itemId, measureLog)
                    }
                }
                3 -> {
                    if (isSPO2Abnormal(measureLog)) {
                        postAlterMessage(itemId, measureLog)
                    }
                }
                5 -> {
                    if (isBodyTemperatureAbnormal(measureLog)) {
                        postAlterMessage(itemId, measureLog)
                    }
                }
            }
            firebaseDataRepository.postMeasureLog(itemId, measureLog)
        }
    }

    private fun postAlterMessage(itemId: String, measureLog: MeasureLog) {
        firebaseDataRepository.postAlertMessage(
            AlertMessage(
                userId = UserManager.UserInfo.id,
                itemId = itemId,
                logId = measureLog.id,
                result = 4,
                createdTime = Timestamp.now()
            )
        )
    }
}
