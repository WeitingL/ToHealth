package com.weiting.tohealth

import com.weiting.tohealth.factory.HomeViewModelFactory
import com.weiting.tohealth.factory.LoginViewModelFactory

fun getVmFactory(): HomeViewModelFactory {
    val repository = PublicApplication.application.firebaseDataRepository
    return HomeViewModelFactory(repository)
}

fun getVmLoginFactory(): LoginViewModelFactory {
    val repository = PublicApplication.application.firebaseDataRepository
    return LoginViewModelFactory(repository)
}
