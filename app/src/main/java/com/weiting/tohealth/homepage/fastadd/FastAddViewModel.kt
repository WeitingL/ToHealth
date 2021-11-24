package com.weiting.tohealth.homepage.fastadd

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.*

class FastAddViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val drugList = firebaseDataRepository
        .getLiveDrugList(UserManager.UserInformation.id!!)

    private val measureList = firebaseDataRepository
        .getLiveMeasureList(UserManager.UserInformation.id!!)

    private val activityList = firebaseDataRepository
        .getLiveActivityList(UserManager.UserInformation.id!!)

    private val drugCurrentList = mutableListOf<FastAddItem>()
    private val measureCurrentList = mutableListOf<FastAddItem>()
    private val activityCurrentList = mutableListOf<FastAddItem>()

    val itemDataMediator = MediatorLiveData<MutableList<FastAddItem>>().apply {
        addSource(drugList) {
            drugCurrentList.clear()
            it.forEach {
                drugCurrentList.add(FastAddItem.DrugItem(it))
            }
            value = (drugCurrentList + measureCurrentList + activityCurrentList).toMutableList()
        }
        addSource(measureList) {
            measureCurrentList.clear()
            it.forEach {
                measureCurrentList.add(FastAddItem.MeasureItem(it))
            }
            value = (drugCurrentList + measureCurrentList + activityCurrentList).toMutableList()
        }
        addSource(activityList) {
            activityCurrentList.clear()
            it.forEach {
                activityCurrentList.add(FastAddItem.ActivityItem(it))
            }
            value = (drugCurrentList + measureCurrentList + activityCurrentList).toMutableList()
        }
    }

    fun postDrugLog(itemId: String, drugLog: DrugLog, drug: Drug) {
        firebaseDataRepository.postDrugRecord(itemId, drugLog)

        val originStock = drug.stock
        val updateStock = originStock - drug.dose

        firebaseDataRepository.editStock(drug.id!!, updateStock)
    }

    fun postActivity(itemId: String, activityLog: ActivityLog) {
        firebaseDataRepository.postActivityRecord(itemId, activityLog)
    }
}

sealed class FastAddItem {
    data class DrugItem(val drug: Drug) : FastAddItem()
    data class MeasureItem(val measure: Measure) : FastAddItem()
    data class ActivityItem(val activity: Activity) : FastAddItem()
}
