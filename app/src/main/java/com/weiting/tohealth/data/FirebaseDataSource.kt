package com.weiting.tohealth.data

import android.util.Log
import com.weiting.tohealth.PublicApplication.Companion.application
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object FirebaseDataSource: FirebaseSource {

    override suspend fun getAllDrugs(): List<Drug> = suspendCoroutine { continuation ->
        val list = mutableListOf<Drug>()
        val database = application.database

        database.collection("drugs")
            .whereEqualTo("userId", "Weiting")
            .get()
            .addOnSuccessListener { result ->

                val dataList = result.toObjects(Drug::class.java)
                list += dataList

//                Log.i("drugsList", list.toString())
                continuation.resume(list)
            }
            .addOnFailureListener { e ->
                Log.w("Error to get data", e)
            }
    }

    override suspend fun getAllMeasures(): List<Measure> = suspendCoroutine { continuation ->
        val list = mutableListOf<Measure>()
        val database = application.database

        database.collection("measures")
            .whereEqualTo("userId", "Weiting")
            .get()
            .addOnSuccessListener { result ->

                val dataList = result.toObjects(Measure::class.java)
                list += dataList

//                Log.i("measuresList", list.toString())
                continuation.resume(list)
            }
            .addOnFailureListener { e ->
                Log.w("Error to get data", e)
            }
    }

    override suspend fun getAllActivities(): List<Activity> = suspendCoroutine { continuation ->
        val list = mutableListOf<Activity>()
        val database = application.database

        database.collection("activity")
            .whereEqualTo("userId", "Weiting")
            .get()
            .addOnSuccessListener { result ->

                val dataList = result.toObjects(Activity::class.java)
                list += dataList

//                Log.i("activityList", list.toString())
                continuation.resume(list)
            }
            .addOnFailureListener { e ->
                Log.w("Error to get data", e)
            }
    }

    override suspend fun getAllCares(): List<Care> = suspendCoroutine { continuation ->
        val list = mutableListOf<Care>()
        val database = application.database

        database.collection("cares")
            .whereEqualTo("userId", "Weiting")
            .get()
            .addOnSuccessListener { result ->

                val dataList = result.toObjects(Care::class.java)
                list += dataList

//                Log.i("caresList", list.toString())
                continuation.resume(list)
            }
            .addOnFailureListener { e ->
                Log.w("Error to get data", e)
            }
    }

    override fun postDrug(drug: Drug) {

        val database = application.database

        drug.id = database.collection("drugs").document().id
        database.collection("drugs").document(drug.id!!)
            .set(drug)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${drug.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postMeasure(measure: Measure) {
        val database = application.database

        measure.id = database.collection("measures").document().id
        database.collection("measures").document(measure.id!!)
            .set(measure)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${measure.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postActivity(activity: Activity) {
        val database = application.database

        activity.id = database.collection("activity").document().id
        database.collection("activity").document(activity.id!!)
            .set(activity)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${activity.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postCare(care: Care) {
        val database = application.database

        care.id = database.collection("cares").document().id
        database.collection("cares").document(care.id!!)
            .set(care)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${care.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postDrugRecord(id: String, drugLog: DrugLog) {

        val database = application.database

        drugLog.id = database.collection("drugs").document(id).collection("drugLogs").document().id
        database.collection("drugs").document(id).collection("drugLogs").document(drugLog.id!!)
            .set(drugLog)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${drugLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postMeasureRecord(id: String, measureLog: MeasureLog) {
        val database = application.database

        measureLog.id = database.collection("measures").document(id).collection("measuresLogs").document().id
        database.collection("measures").document(id).collection("measuresLogs").document(measureLog.id!!)
            .set(measureLog)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${measureLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postActivityRecord(id: String, activityLog: ActivityLog) {
        val database = application.database

        activityLog.id = database.collection("activity").document(id).collection("activityLogs").document().id
        database.collection("activity").document(id).collection("activityLogs").document(activityLog.id!!)
            .set(activityLog)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${activityLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }

    override fun postCareRecord(id: String, careLog: CareLog) {
        val database = application.database

        careLog.id = database.collection("cares").document(id).collection("careLogs").document().id
        database.collection("cares").document(id).collection("careLogs").document(careLog.id!!)
            .set(careLog)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${careLog.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }


}