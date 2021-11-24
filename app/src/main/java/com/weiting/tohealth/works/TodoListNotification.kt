package com.weiting.tohealth.works

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.*
import com.weiting.tohealth.data.*
import com.weiting.tohealth.util.ItemArranger
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt
import com.weiting.tohealth.util.Util.toActivityType
import com.weiting.tohealth.util.Util.toCareType
import com.weiting.tohealth.util.Util.toMeasureType
import com.weiting.tohealth.util.Util.toTimeFromTimeStamp
import com.weiting.tohealth.util.Util.toUnit

const val CHANNEL_ID = "toHealth"

class TodoListNotification {

    private val context = PublicApplication.application.applicationContext

    suspend fun receiveIntentFromAlarm(timeTag: Int, firebaseDataRepository: FirebaseRepository) {
        val userId = Firebase.auth.currentUser?.uid?:""

        val drugList = firebaseDataRepository.getAllDrugs(userId).filter {
            ItemArranger().isThatDayNeedToDo(
                ItemType.DRUG, ItemData(DrugData = it),
                Timestamp.now()
            )
        }
        drugList.forEach { drug ->
            drug.executedTime.forEach { timeStamp ->
                if (getTimeStampToTimeInt(timeStamp) == timeTag) {
                    showNotification(drug, timeStamp)
                }
            }
        }

        val measureList = firebaseDataRepository.getAllMeasures(userId).filter {
            ItemArranger().isThatDayNeedToDo(
                ItemType.MEASURE, ItemData(MeasureData = it),
                Timestamp.now()
            )
        }
        measureList.forEach { measure ->
            measure.executedTime.forEach { timestamp ->
                if (getTimeStampToTimeInt(timestamp) == timeTag) {
                    showNotification(measure, timestamp)
                }
            }
        }

        val careList = firebaseDataRepository.getAllCares(userId).filter {
            ItemArranger().isThatDayNeedToDo(
                ItemType.CARE, ItemData(CareData = it),
                Timestamp.now()
            )
        }
        careList.forEach { care ->
            care.executeTime.forEach { timestamp ->
                if (getTimeStampToTimeInt(timestamp) == timeTag) {
                    showNotification(care, timestamp)
                }
            }
        }

        val activityList = firebaseDataRepository.getAllActivities(userId).filter {
            ItemArranger().isThatDayNeedToDo(
                ItemType.ACTIVITY, ItemData(ActivityData = it),
                Timestamp.now()
            )
        }
        activityList.forEach { activity ->
            activity.executedTime.forEach { timestamp ->
                if (getTimeStampToTimeInt(timestamp) == timeTag) {
                    showNotification(activity, timestamp)
                }
            }
        }
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_ID,
        NotificationManager.IMPORTANCE_HIGH
    )
    private val mainIntent = Intent(context, MainActivity::class.java)
    private val pendingIntent =
        PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_CANCEL_CURRENT)

    private fun showNotification(itemsDataType: ItemsDataType, timestamp: Timestamp) {
        notificationManager.createNotificationChannel(channel)

        when (itemsDataType) {
            is Drug -> {
                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.hospital_sign)
                    .setContentTitle("已經${toTimeFromTimeStamp(Timestamp.now())}  該吃藥囉!")
                    .setContentText(
                        "${itemsDataType.drugName}\n要吃 ${itemsDataType.dose} ${toUnit(itemsDataType.unit)}\n輕觸可以開啟應用程式"
                    )
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .build()

                notificationManager.notify(timestamp.nanoseconds, notification)
            }

            is Measure -> {
                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.hospital_sign)
                    .setContentTitle("已經${toTimeFromTimeStamp(Timestamp.now())}囉!")
                    .setContentText(
                        "應該要量${toMeasureType(itemsDataType.type)}了!\n輕觸可以開啟應用程式"
                    )
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .build()

                notificationManager.notify(timestamp.nanoseconds + 1, notification)
            }

            is Care -> {
                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.hospital_sign)
                    .setContentTitle("已經${toTimeFromTimeStamp(Timestamp.now())}囉!")
                    .setContentText(
                        "應該要填${toCareType(itemsDataType.type)}了!\n輕觸可以開啟應用程式"
                    )
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .build()

                notificationManager.notify(timestamp.nanoseconds + 2, notification)
            }
            is Activity -> {
                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.hospital_sign)
                    .setContentTitle("已經${toTimeFromTimeStamp(Timestamp.now())}囉!")
                    .setContentText(
                        "應該要去${toActivityType(itemsDataType.type)}了!\n輕觸可以開啟應用程式"
                    )
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .build()

                notificationManager.notify(timestamp.nanoseconds + 3, notification)
            }
        }
    }
}