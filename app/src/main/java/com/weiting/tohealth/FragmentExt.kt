package com.weiting.tohealth

import androidx.fragment.app.Fragment
import com.weiting.tohealth.factory.HomeViewModelFactory

fun Fragment.getVmFactory(): HomeViewModelFactory{
    val repository = PublicApplication.application.firebaseDataRepository
    return HomeViewModelFactory(repository)
}