package com.weiting.tohealth

import android.content.Context
import com.weiting.tohealth.data.FirebaseDataRepository
import com.weiting.tohealth.data.FirebaseDataSource
import com.weiting.tohealth.data.FirebaseRepository

object FirebaseLocator {

    @Volatile
    var firebaseRepository: FirebaseRepository? = null

    fun provideRepository(context: Context): FirebaseRepository{
        synchronized(this){
            return firebaseRepository?: createRepository(context)
        }
    }

    private fun createRepository(context: Context): FirebaseRepository{
        return FirebaseDataRepository(FirebaseDataSource)
    }

}