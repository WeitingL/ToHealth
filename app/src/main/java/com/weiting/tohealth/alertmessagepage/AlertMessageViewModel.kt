package com.weiting.tohealth.alertmessagepage

import androidx.lifecycle.*
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.AlertMessage
import com.weiting.tohealth.data.Result
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
            list.forEach { it ->

                val user = when (val result = firebaseDataRepository.getUser(it.userId ?: "")) {
                    is Result.Success -> result.data
                    else -> null
                }

                when (it.result) {
                    4 -> {
                        val measure =
                            when (val result = firebaseDataRepository.getMeasure(it.itemId ?: "")) {
                                is Result.Success -> result.data
                                else -> null
                            }

                        val measureLog = when (val result = firebaseDataRepository.getMeasureLog(
                            it.itemId ?: "",
                            it.logId ?: ""
                        )) {
                            is Result.Success -> result.data
                            else -> null
                        }

                        if (measure != null && measureLog != null) {
                            recordList.add(
                                AlterMessageRecord(
                                    title = "????????????",
                                    type = 4,
                                    itemName = "${user?.name} ${
                                        toNotificationTextForMeasureLog(
                                            measure,
                                            measureLog
                                        )
                                    }",
                                    createdTime = getTimeStampToDateAndTimeString(it.createdTime)
                                )
                            )
                        }

                    }
                    5 -> {
                        recordList.add(
                            AlterMessageRecord(
                                title = "???????????????",
                                type = 5,
                                itemName = "${user?.name} ???????????????????????????!",
                                createdTime = getTimeStampToDateAndTimeString(it.createdTime)
                            )
                        )
                    }
                    6 -> {
                        val drug =
                            when (val result = firebaseDataRepository.getDrug(it.itemId ?: "")) {
                                is Result.Success -> result.data
                                else -> null
                            }

                        recordList.add(
                            AlterMessageRecord(
                                title = "??????????????????",
                                type = 6,
                                itemName = "${user?.name} ??? ${drug?.drugName} ???????????????!" +
                                        "\n?????? ${(drug?.stock ?: 0F / (drug?.dose ?: 0F)).toInt()} ????????????",
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
