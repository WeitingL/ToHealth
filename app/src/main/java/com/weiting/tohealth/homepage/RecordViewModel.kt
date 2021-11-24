package com.weiting.tohealth.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.*
import com.weiting.tohealth.util.NotificationGenerator
import kotlinx.coroutines.launch

class RecordViewModel(private val firebaseDataRepository: FirebaseRepository) :
    ViewModel(),
    NotificationGenerator {

    var careScore: Int? = null
    var careInfo: String? = null

    fun getCareScore(int: Int) {
        careScore = int
    }

    fun getCareInfo(note: String) {
        careInfo = note
    }

    fun postCareLog(itemId: String, careLog: CareLog) {
        careLog.record = mapOf("emotion" to careScore?.toString(), "note" to careInfo)
        firebaseDataRepository.postCareRecord(itemId, careLog)
    }

    fun postMeasureLog(itemId: String, measureLog: MeasureLog, measureType: Int) {
        viewModelScope.launch {
            measureLog.id = firebaseDataRepository.getMeasureRecordId(itemId)
            when (measureType) {
                0 -> {
                    if (isBloodPressureAbnormal(measureLog)) {
                        postNotification(itemId, measureLog)
                    }
                }
                1 -> {
                    if (isBeforeMealBloodSugarAbnormal(measureLog)) {
                        postNotification(itemId, measureLog)
                    }
                }
                2 -> {
                    if (isAfterMealBloodSugarAbnormal(measureLog)) {
                        postNotification(itemId, measureLog)
                    }
                }
                3 -> {
                    if (isSPO2Abnormal(measureLog)) {
                        postNotification(itemId, measureLog)
                    }
                }
                5 -> {
                    if (isBodyTemperatureAbnormal(measureLog)) {
                        postNotification(itemId, measureLog)
                    }
                }
            }
            firebaseDataRepository.postMeasureRecord(itemId, measureLog)
        }
    }

    private fun postNotification(itemId: String, measureLog: MeasureLog) {
        firebaseDataRepository.postNotification(
            Notification(
                userId = UserManager.UserInformation.id,
                itemId = itemId,
                logId = measureLog.id,
                result = 4,
                createdTime = Timestamp.now()
            )
        )
    }
}
