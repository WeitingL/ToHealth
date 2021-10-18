package com.weiting.tohealth.data

import android.util.Log
import com.weiting.tohealth.application

object FirebaseDataSource: FirebaseSource {

    override fun getAllDrugs(): List<Drug> {
        val list = mutableListOf<Drug>()
        val database = application.database

        database.collection("drugs")
            .get()
            .addOnSuccessListener { result ->

                val dataList = result.toObjects(Drug::class.java)
                list += dataList

            }
            .addOnFailureListener { e ->
                Log.w("Error to get data", e)
            }

        return list
    }

    override fun postDrug(drug: Drug) {

        val database = application.database

        drug.id = database.collection("Drugs").document().id
        database.collection("Drugs").document(drug.id!!)
            .set(drug)
            .addOnSuccessListener { documentReference ->
                Log.d("store success", "DocumentSnapshot added with ID: ${drug.id}")
            }
            .addOnFailureListener { e ->
                Log.w("store failure", "Error adding document", e)
            }
    }
}