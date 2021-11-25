package com.weiting.tohealth.homepage.fastadd

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.*

class FastAddViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val drugs = firebaseDataRepository
        .getLiveDrugs(UserManager.UserInfo.id!!)

    private val measures = firebaseDataRepository
        .getLiveMeasures(UserManager.UserInfo.id!!)

    private val events = firebaseDataRepository
        .getLiveEvents(UserManager.UserInfo.id!!)

    private val drugCurrentList = mutableListOf<ItemData>()
    private val measureCurrentList = mutableListOf<ItemData>()
    private val eventCurrentList = mutableListOf<ItemData>()

    val itemDataMediator = MediatorLiveData<MutableList<ItemData>>().apply {
        addSource(drugs) {
            drugCurrentList.clear()
            it.forEach {
                drugCurrentList.add(ItemData(DrugData = it))
            }
            value = (drugCurrentList + measureCurrentList + eventCurrentList).toMutableList()
        }
        addSource(measures) {
            measureCurrentList.clear()
            it.forEach {
                measureCurrentList.add(ItemData(MeasureData = it))
            }
            value = (drugCurrentList + measureCurrentList + eventCurrentList).toMutableList()
        }
        addSource(events) {
            eventCurrentList.clear()
            it.forEach {
                eventCurrentList.add(ItemData(EventData = it))
            }
            value = (drugCurrentList + measureCurrentList + eventCurrentList).toMutableList()
        }
    }

    fun postDrugLog(itemId: String, drugLog: DrugLog, drug: Drug) {
        firebaseDataRepository.postDrugLog(itemId, drugLog)

        val originStock = drug.stock
        val updateStock = originStock - drug.dose

        firebaseDataRepository.editStock(drug.id?:"", updateStock)
    }

    fun postActivity(itemId: String, eventLog: EventLog) {
        firebaseDataRepository.postEventLog(itemId, eventLog)
    }
}
