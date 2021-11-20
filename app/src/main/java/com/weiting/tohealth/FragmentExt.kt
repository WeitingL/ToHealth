package com.weiting.tohealth

import androidx.fragment.app.Fragment
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.data.User
import com.weiting.tohealth.factory.HomeViewModelFactory
import com.weiting.tohealth.factory.ItemUpdateViewModelFactory
import com.weiting.tohealth.factory.LoginViewModelFactory
import com.weiting.tohealth.mymanagepage.ManageType

fun Fragment.getVmFactory(): HomeViewModelFactory{
    val repository = PublicApplication.application.firebaseDataRepository
    return HomeViewModelFactory(repository)
}

fun Fragment.getVmLoginFactory(): LoginViewModelFactory {
    val repository = PublicApplication.application.firebaseDataRepository
    return LoginViewModelFactory(repository)
}