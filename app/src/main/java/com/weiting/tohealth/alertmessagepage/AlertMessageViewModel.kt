package com.weiting.tohealth.alertmessagepage

import androidx.lifecycle.*
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.AlertMessage
import com.weiting.tohealth.util.Util.toNotificationTextForMeasureLog
import com.weiting.tohealth.util.Util.getTimeStampToDateAndTimeString
import kotlinx.coroutines.launch

class AlertMessageViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    memberList: List<String>
) : ViewModel() {

    val list = when (memberList.isEmpty()) {
        true -> listOf("")
        false -> memberList
    }

    val liveAlterMessageRecord = firebaseDataRepository.getLiveAlertMessages(list)

    private val _alterMessageRecordList = MutableLiveData<MutableList<AlterMessageRecord>>()
    val alterMessageRecordList: LiveData<MutableList<AlterMessageRecord>>
        get() = _alterMessageRecordList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        _alterMessageRecordList.value = mutableListOf()
        _isLoading.value = true
    }

    fun transferToAlterMessageRecord(list: List<AlertMessage>) {
        viewModelScope.launch {
            val recordList = mutableListOf<AlterMessageRecord>()
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
                            AlterMessageRecord(
                                title = "數據異常",
                                type = 4,
                                itemName = "${user.name} ${
                                toNotificationTextForMeasureLog(
                                    measure,
                                    measureLog
                                )
                                }",
                                createdTime = getTimeStampToDateAndTimeString(it.createdTime)
                            )
                        )
                    }
                    5 -> {
                        recordList.add(
                            AlterMessageRecord(
                                title = "多次未完成",
                                type = 5,
                                itemName = "${user.name} 昨天多次未完成事項!",
                                createdTime = getTimeStampToDateAndTimeString(it.createdTime)
                            )
                        )
                    }
                    6 -> {
                        val drug = firebaseDataRepository.getDrug(it.itemId!!)
                        recordList.add(
                            AlterMessageRecord(
                                title = "藥物儲量警報",
                                type = 6,
                                itemName = "${user.name} 的 ${drug.drugName} 快要用完了!" +
                                    "\n剩下 ${(drug.stock / drug.dose).toInt()} 次的服藥",
                                createdTime = getTimeStampToDateAndTimeString(it.createdTime)
                            )
                        )
                    }
                    else -> throw Exception("Error result")
                }
            }
            _alterMessageRecordList.value = recordList
            _isLoading.value = false
        }
    }
}

data class AlterMessageRecord(
    val title: String,
    val type: Int,
    val itemName: String,
    val createdTime: String
)
