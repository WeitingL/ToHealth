package com.weiting.tohealth.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.Drug

class HomeViewModel : ViewModel() {

    private val _nextTaskList = MutableLiveData<List<HomePageItem>>()
    val nextTaskList: LiveData<List<HomePageItem>>
        get() = _nextTaskList

    private val list = listOf<Drug>(
        Drug(
            drug = "Aspirin",
            unit = "一顆",
            time = "08:00",
            stock = "剩餘30顆"
        ),
        Drug(
            drug = "Aspirin",
            unit = "一顆",
            time = "08:00",
            stock = "剩餘30顆"
        ),
        Drug(
            drug = "Aspirin",
            unit = "一顆",
            time = "08:00",
            stock = "剩餘30顆"
        ),
        Drug(
            drug = "Aspirin",
            unit = "一顆",
            time = "08:00",
            stock = "剩餘30顆"
        ),
        Drug(
            drug = "Aspirin",
            unit = "一顆",
            time = "08:00",
            stock = "剩餘30顆"
        ),
        Drug(
            drug = "Aspirin",
            unit = "一顆",
            time = "08:00",
            stock = "剩餘30顆"
        ),
        Drug(
            drug = "Aspirin",
            unit = "一顆",
            time = "08:00",
            stock = "剩餘30顆"
        ),
        Drug(
            drug = "Aspirin",
            unit = "一顆",
            time = "08:00",
            stock = "剩餘30顆"
        )
    )

    init {
        _nextTaskList.value = listOf(
            HomePageItem.AddNewItem,
            HomePageItem.TodayAbstract,
            HomePageItem.NextTask(list),
            HomePageItem.MyGroupNews
        )
    }
}

sealed class HomePageItem {

    object AddNewItem : HomePageItem()

    object TodayAbstract : HomePageItem()

    // Need to design what info need to show!
    object MyGroupNews : HomePageItem()

    data class NextTask(val list: List<Drug>) : HomePageItem()

}