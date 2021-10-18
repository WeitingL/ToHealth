package com.weiting.tohealth

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.weiting.tohealth.data.FirebaseRepository

lateinit var application: PublicApplication

class PublicApplication: Application() {

    lateinit var database : FirebaseFirestore

    val firebaseDataRepository: FirebaseRepository
        get() = FirebaseLocator.provideRepository(this)

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        database = FirebaseFirestore.getInstance()

        application = this
    }
}