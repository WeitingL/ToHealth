package com.weiting.tohealth.mystatisticpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatisticDetailViewModel : ViewModel() {

    private val _logList = MutableLiveData<List<LogItem>>()
    val logList: LiveData<List<LogItem>>
        get() = _logList

    private val list = listOf<LogItem>(
        LogItem.Item, LogItem.Item, LogItem.Item, LogItem.Bottom
    )

    init {
        _logList.value = list
    }

}

sealed class LogItem {
    object Item : LogItem()
    object Bottom: LogItem()
}