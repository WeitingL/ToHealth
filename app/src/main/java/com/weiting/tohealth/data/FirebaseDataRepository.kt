package com.weiting.tohealth.data

class FirebaseDataRepository(private val firebaseSource: FirebaseSource): FirebaseRepository {

    override suspend fun getAllDrugs(): List<Drug> {
        return firebaseSource.getAllDrugs()
    }

    override fun postDrug(drug: Drug) {
        return firebaseSource.postDrug(drug)
    }
}