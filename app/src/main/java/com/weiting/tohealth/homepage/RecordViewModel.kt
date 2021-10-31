package com.weiting.tohealth.homepage

import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.CareLog
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.MeasureLog
import kotlin.concurrent.fixedRateTimer

class RecordViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    var careScore: Int? = null
    var careInfo: String? = null

    fun getCareScore(int: Int) {
        careScore = int
    }

    fun getCareInfo(note: String) {
        careInfo = note
    }

    fun postCareLog(itemId: String, careLog: CareLog){
        careLog.record = mapOf("emotion" to careScore?.toString(), "note" to careInfo)
        firebaseDataRepository.postCareRecord(itemId, careLog)
    }

    fun postMeasureLog(itemId: String, measureLog: MeasureLog){
        firebaseDataRepository.postMeasureRecord(itemId, measureLog)
    }


}