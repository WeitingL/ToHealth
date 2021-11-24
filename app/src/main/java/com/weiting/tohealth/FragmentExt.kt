package com.weiting.tohealth

import androidx.fragment.app.Fragment
import com.weiting.tohealth.factory.HomeViewModelFactory
import com.weiting.tohealth.factory.LoginViewModelFactory

fun Fragment.getVmFactory(): HomeViewModelFactory {
    val repository = PublicApplication.application.firebaseDataRepository
    return HomeViewModelFactory(repository)
}

fun Fragment.getVmLoginFactory(): LoginViewModelFactory {
    val repository = PublicApplication.application.firebaseDataRepository
    return LoginViewModelFactory(repository)
}
