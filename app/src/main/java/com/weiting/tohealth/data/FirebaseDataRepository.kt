package com.weiting.tohealth.data

class FirebaseDataRepository(private val firebaseSource: FirebaseSource): FirebaseRepository {

    override suspend fun getAllDrugs(): List<Drug> {
        return firebaseSource.getAllDrugs()
    }

    override suspend fun getAllMeasures(): List<Measure> {
        return firebaseSource.getAllMeasures()
    }

    override suspend fun getAllActivities(): List<Activity> {
        return firebaseSource.getAllActivities()
    }

    override suspend fun getAllCares(): List<Care> {
        return firebaseSource.getAllCares()
    }

    override fun postDrug(drug: Drug) {
        return firebaseSource.postDrug(drug)
    }

    override fun postMeasure(measure: Measure) {
        return firebaseSource.postMeasure(measure)
    }

    override fun postActivity(activity: Activity) {
        return firebaseSource.postActivity(activity)
    }

    override fun postCare(care: Care) {
        return firebaseSource.postCare(care)
    }
}