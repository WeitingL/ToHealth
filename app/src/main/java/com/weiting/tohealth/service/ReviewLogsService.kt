package com.weiting.tohealth.service

import android.app.Service
import android.content.Intent
import android.os.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.ItemArranger
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.*
import com.weiting.tohealth.getTimeStampToDateInt
import com.weiting.tohealth.getTimeStampToTimeInt
import kotlinx.coroutines.*

class ReviewLogsService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    lateinit var firebaseDataRepository: FirebaseRepository

    //Avoid to occupy the main thread.
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            try {
                firebaseDataRepository = PublicApplication.application.firebaseDataRepository
                val loadingUnCheckLogs = LoadingUnCheckLogs(firebaseDataRepository)
                loadingUnCheckLogs.postAllUnCheckLogs()

            } catch (e: InterruptedException) {
                //If the connection is error, the the thread interrupt.
                Thread.currentThread().interrupt()
            }
        }
    }

    private inner class LoadingUnCheckLogs(private val firebaseDataRepository: FirebaseRepository) {

        private val job = SupervisorJob()
        private val scope = CoroutineScope(Dispatchers.Main + job)

        private val user = Firebase.auth.currentUser

        private lateinit var userId: String

        private val itemList = mutableListOf<ItemsDataType>()

        fun postAllUnCheckLogs() {
            getUserId()
            getItemsLog()
            getUncheckLogs()

        }

        private fun getUserId() {
            if (user != null) {
                userId = user.uid
            } else {
                onDestroy()
            }
        }

        private fun getItemsLog() {
            scope.launch {

                val drugList = firebaseDataRepository.getAllDrugs(userId).filter {
                    ItemArranger().isTodayNeedToDo(ItemType.DRUG, ItemData(DrugData = it))
                }
                drugList.forEach {
                    it.drugLogs =
                        firebaseDataRepository.getDrugRecord(it.id!!, Timestamp.now()).filter {
                            getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(
                                Timestamp.now()
                            )
                        }
                    itemList.add(it)
                }

                val measureList = firebaseDataRepository.getAllMeasures(userId).filter {
                    ItemArranger().isTodayNeedToDo(ItemType.MEASURE, ItemData(MeasureData = it))
                }
                measureList.forEach {
                    it.measureLogs.filter {
                        getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(
                            Timestamp.now()
                        )
                    }
                    itemList.add(it)
                }

                val careList = firebaseDataRepository.getAllCares(userId).filter {
                    ItemArranger().isTodayNeedToDo(ItemType.CARE, ItemData(CareData = it))
                }
                careList.forEach {
                    it.careLogs =
                        firebaseDataRepository.getCareRecord(it.id!!, Timestamp.now()).filter {
                            getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(
                                Timestamp.now()
                            )
                        }
                    itemList.add(it)
                }

                val activityList = firebaseDataRepository.getAllActivities(userId).filter {
                    ItemArranger().isTodayNeedToDo(ItemType.ACTIVITY, ItemData(ActivityData = it))
                }
                activityList.forEach {
                    it.activityLogs =
                        firebaseDataRepository.getActivityRecord(it.id!!, Timestamp.now()).filter {
                            getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(
                                Timestamp.now()
                            )
                        }
                    itemList.add(it)
                }
            }
        }

        private fun getUncheckLogs() {
            val timeTags = mutableListOf<Int>()

            itemList.forEach {
                when (it) {
                    is Drug -> {
                        it.drugLogs.forEach { drugLog ->
                            timeTags.add(drugLog.timeTag!!)
                        }
                        it.executedTime.forEach { timeStamp ->
                            if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                                firebaseDataRepository.postDrugRecord(
                                    it.id!!, DrugLog(
                                        timeTag = getTimeStampToTimeInt(timeStamp),
                                        result = 3,
                                        createdTime = Timestamp.now()
                                    )
                                )
                            }
                        }
                        timeTags.clear()
                    }

                    is Activity -> {
                        it.activityLogs.forEach { activityLog ->
                            timeTags.add(activityLog.timeTag!!)
                        }
                        it.executedTime.forEach { timeStamp ->
                            if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                                firebaseDataRepository.postActivityRecord(
                                    it.id!!, ActivityLog(
                                        timeTag = getTimeStampToTimeInt(timeStamp),
                                        result = 3,
                                        createdTime = Timestamp.now(),
                                    )
                                )
                            }
                        }
                        timeTags.clear()
                    }

                    is Measure -> {
                        it.measureLogs.forEach { measureLog ->
                            timeTags.add(measureLog.timeTag!!)
                        }
                        it.executedTime.forEach { timeStamp ->
                            if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                                firebaseDataRepository.postMeasureRecord(
                                    it.id!!,
                                    MeasureLog(
                                        timeTag = getTimeStampToTimeInt(timeStamp),
                                        result = 3,
                                        createdTime = Timestamp.now()
                                    )
                                )
                            }
                        }
                        timeTags.clear()
                    }

                    is Care -> {
                        it.careLogs.forEach { careLog ->
                            timeTags.add(careLog.timeTag!!)
                        }
                        it.executeTime.forEach { timeStamp ->
                            if (getTimeStampToTimeInt(timeStamp) !in timeTags) {
                                firebaseDataRepository.postCareRecord(
                                    it.id!!,
                                    CareLog(
                                        timeTag = getTimeStampToTimeInt(timeStamp),
                                        result = 3,
                                        createdTime = Timestamp.now()
                                    )
                                )
                            }
                        }
                        timeTags.clear()
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        HandlerThread("PostUncheckLog", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}