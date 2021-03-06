package com.weiting.tohealth

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.weiting.tohealth.data.FirebaseRepository
import kotlin.properties.Delegates

class PublicApplication : Application() {

    lateinit var database: FirebaseFirestore

    val firebaseDataRepository: FirebaseRepository
        get() = FirebaseLocator.provideRepository()

    companion object {
        var application: PublicApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        database = FirebaseFirestore.getInstance()
        application = this
    }
}
