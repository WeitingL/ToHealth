package com.weiting.tohealth

import androidx.fragment.app.Fragment
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.data.User
import com.weiting.tohealth.factory.HomeViewModelFactory
import com.weiting.tohealth.factory.ItemUpdateViewModelFactory
import com.weiting.tohealth.mymanagepage.ManageType

fun Fragment.getVmFactory(): HomeViewModelFactory{
    val repository = PublicApplication.application.firebaseDataRepository
    return HomeViewModelFactory(repository)
}

fun Fragment.getVmFactory(manageType: ManageType, itemData: ItemData, user: User): ItemUpdateViewModelFactory{
    val repository = PublicApplication.application.firebaseDataRepository
    return ItemUpdateViewModelFactory(repository, itemData, manageType, user)
}