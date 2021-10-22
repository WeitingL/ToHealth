package com.weiting.tohealth.homepage.recorddialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.*

class RecordViewModel(private val firebaseDataRepository: FirebaseRepository) : ViewModel() {

    private val _careScore = MutableLiveData<Int>()
    val careScore: LiveData<Int>
        get() = _careScore

    private val _careInfo = MutableLiveData<String>()
    val careInfo: LiveData<String>
        get() = _careInfo

    private val _measureNumOne = MutableLiveData<Int>()
    val measureNumOne: LiveData<Int>
        get() = _measureNumOne

    private val _measureNumTwo = MutableLiveData<Int?>()
    val measureNumTwo: LiveData<Int?>
        get() = _measureNumTwo

    private val _measureNumThree = MutableLiveData<Int?>()
    val measureNumThree: LiveData<Int?>
        get() = _measureNumThree

    fun getCareScore(int: Int) {
        _careScore.value = int
    }

    fun getInfo(string: String) {
        _careInfo.value = string
    }

    fun getMeasureData(one: Int, two: Int?, three: Int?) {
        _measureNumOne.value = one
        _measureNumTwo.value = two
        _measureNumThree.value = three
    }

    fun postRecord(itemType: ItemType, id: String) {
        when (itemType) {
            ItemType.DRUG -> {
                firebaseDataRepository.postDrugRecord(
                    id, DrugLog(
                        result = 2,
                        createTime = Timestamp.now()
                    )
                )
            }

            ItemType.CARE -> {
                firebaseDataRepository.postCareRecord(
                    id, CareLog(
                        record = mapOf(
                            "emotion" to careScore.value.toString(),
                            "note" to careInfo.value
                        ),
                        result = 2,
                        createTime = Timestamp.now()
                    )
                )
            }

            ItemType.ACTIVITY -> {
                firebaseDataRepository.postActivityRecord(
                    id, ActivityLog(
                        result = 2,
                        createTime = Timestamp.now()
                    )
                )
            }
            ItemType.MEASURE -> {
                firebaseDataRepository.postMeasureRecord(
                    id, MeasureLog(
                        result = 2,
                        record = mapOf(
                            "X" to measureNumOne.value,
                            "Y" to measureNumTwo.value,
                            "Z" to measureNumThree.value
                        ),
                        createTime = Timestamp.now()
                    )
                )
            }
        }
    }
}