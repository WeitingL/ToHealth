package com.weiting.tohealth

import android.content.Context
import com.weiting.tohealth.data.FirebaseDataRepository
import com.weiting.tohealth.data.FirebaseDataSource
import com.weiting.tohealth.data.FirebaseRepository

object FirebaseLocator {

    @Volatile
    var firebaseRepository: FirebaseRepository? = null

    fun provideRepository(): FirebaseRepository {
        synchronized(this) {
            return firebaseRepository ?: createRepository()
        }
    }

    private fun createRepository(): FirebaseRepository {
        return FirebaseDataRepository(FirebaseDataSource)
    }
}
