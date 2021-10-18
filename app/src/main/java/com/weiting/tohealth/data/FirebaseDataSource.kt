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
            .get()
            .addOnSuccessListener { result ->

                val dataList = result.toObjects(Drug::class.java)
                list += dataList

                Log.i("Data?", list.toString())
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
}