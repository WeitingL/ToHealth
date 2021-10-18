package com.weiting.tohealth.data

interface FirebaseSource {

    suspend fun getAllDrugs(): List<Drug>

//    fun getAllMeasures(): List<Measure>
//
//    fun getAllActivities(): List<Activity>
//
//    fun getAllCares(): List<Care>

    fun postDrug(drug: Drug)

//    fun postMeasure(measure: Measure)
//
//    fun postActivity(activity: Activity)
//
//    fun postCare(care: Care)

}