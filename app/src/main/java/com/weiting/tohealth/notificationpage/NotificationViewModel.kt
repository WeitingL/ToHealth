package com.weiting.tohealth.notificationpage

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Notification
import com.weiting.tohealth.toNotificationTextForMeasureLog
import com.weiting.tohealth.toStringFromTimeStamp
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val memberList: List<String>
) : ViewModel() {

    val notificationList = firebaseDataRepository.getLiveNotification(memberList)

    private val _notificationRecordList = MutableLiveData<MutableList<NotificationRecord>>()
    val notificationRecordList: LiveData<MutableList<NotificationRecord>>
        get() = _notificationRecordList

    init {
        _notificationRecordList.value = mutableListOf()
    }

    fun transferToNotificationRecord(list: List<Notification>) {
        viewModelScope.launch {
            val recordList = mutableListOf<NotificationRecord>()
            list.forEach {
                val user = firebaseDataRepository.getUser(it.userId!!)
                when (it.result) {
                    4 -> {
                        val measure = firebaseDataRepository.getMeasure(it.itemId!!)
                        val measureLog = firebaseDataRepository.getMeasureLog(
                            it.itemId,
                            it.logId!!
                        )
                        recordList.add(
                            NotificationRecord(
                                title = "數據異常",
                                type = 4,
                                itemName = "${user.name} ${
                                    toNotificationTextForMeasureLog(
                                        measure,
                                        measureLog
                                    )
                                }",
                                createdTime = toStringFromTimeStamp(it.createdTime)
                            )
                        )
                    }
                    5 -> {
                        recordList.add(
                            NotificationRecord(
                                title = "多次未完成",
                                type = 5,
                                itemName = "${user.name} 昨天多次未完成事項!",
                                createdTime = toStringFromTimeStamp(it.createdTime)
                            )
                        )
                    }
                    6 -> {
                        val drug = firebaseDataRepository.getDrug(it.itemId!!)
                        recordList.add(
                            NotificationRecord(
                                title = "藥物儲量警報",
                                type = 6,
                                itemName = "${user.name} \n${drug.drugName} 快要用完了!" +
                                        "\n剩下 ${(drug.stock / drug.dose).toInt()} 次的服藥",
                                createdTime = toStringFromTimeStamp(it.createdTime)
                            )
                        )
                    }
                    else -> throw Exception("Error result")
                }
            }
            _notificationRecordList.value = recordList
        }
    }
}


data class NotificationRecord(
    val title: String,
    val type: Int,
    val itemName: String,
    val createdTime: String
)