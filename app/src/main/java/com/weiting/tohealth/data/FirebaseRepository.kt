package com.weiting.tohealth.data

interface FirebaseRepository {

    suspend fun getAllDrugs(): List<Drug>

    suspend fun getAllMeasures(): List<Measure>

    suspend fun getAllActivities(): List<Activity>

    suspend fun getAllCares(): List<Care>

    fun postDrug(drug: Drug)

    fun postMeasure(measure: Measure)

    fun postActivity(activity: Activity)

    fun postCare(care: Care)

}