package com.weiting.tohealth.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Drug
import com.weiting.tohealth.data.FirebaseRepository
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val _nextTaskList = MutableLiveData<List<HomePageItem>>()
    val nextTaskList: LiveData<List<HomePageItem>>
        get() = _nextTaskList

    private val list = mutableListOf<Drug>()

    init {
        _nextTaskList.value = listOf(
            HomePageItem.AddNewItem,
            HomePageItem.TodayAbstract
        )

        getAllDrugs()
    }

    private fun getAllDrugs() {
         viewModelScope.launch{
            list += firebaseDataRepository.getAllDrugs()

            if (list.isNotEmpty()) {
                _nextTaskList.value = _nextTaskList.value?.plus(HomePageItem.NextTask(list))
            }
        }
    }
}

sealed class HomePageItem() {

    object AddNewItem : HomePageItem()

    object TodayAbstract : HomePageItem()

    // Need to design what info need to show!
    object MyGroupNews : HomePageItem()

    data class NextTask(val list: List<Drug>) : HomePageItem()
}


//private fun postDrug() {
//        firebaseDataRepository.postDrug(
//            Drug(
//                id = null,
//                userId = "test",
//                drugName = "Aspirin",
//                dose = 1,
//                unit = 1,
//                endDate = mapOf("type" to 1, "day" to 1),
//                period = mapOf("type" to 1, "N" to 3, "X" to null, "Y" to null),
//                firstTimePerDay = 800,
//                stock = 30,
//                editor = "myTest",
//                createTime = Timestamp.now(),
//                status = 1,
//                drugLogs = listOf()
//            )
//        )
//    }