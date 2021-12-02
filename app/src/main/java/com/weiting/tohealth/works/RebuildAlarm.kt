package com.weiting.tohealth.works

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.data.Result
import com.weiting.tohealth.receiver.AlarmReceiver
import com.weiting.tohealth.receiver.POST_NOTIFICATION
import com.weiting.tohealth.util.ItemArranger
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt
import java.util.*
import kotlin.system.measureNanoTime

class RebuildAlarm {

    /*
        Only apply the alarm with timeStamp
     */

    suspend fun updateNewTodoListToAlarmManager(firebaseDataRepository: FirebaseRepository) {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        val timeList = mutableListOf<Timestamp>()

        val drugs = when(val result = firebaseDataRepository.getAllDrugs(userId)){
            is Result.Success -> result.data
            else -> listOf()
        }
        val drugList = drugs.filter {
            ItemArranger.isThatDayNeedToDo(ItemData(drugData = it), Timestamp.now())
        }
        drugList.forEach { drug ->
            drug.executedTime.forEach {
                if (getTimeStampToTimeInt(it) > getTimeStampToTimeInt(Timestamp.now())) {
                    timeList.add(it)
                }
            }
        }

        val measures = when(val result = firebaseDataRepository.getAllMeasures(userId)) {
            is Result.Success -> result.data
            else -> listOf()
        }
        val measureLog = measures.filter {
            ItemArranger.isThatDayNeedToDo(ItemData(measureData = it), Timestamp.now())
        }
        measureLog.forEach { measure ->
            measure.executedTime.forEach {
                if (getTimeStampToTimeInt(it) > getTimeStampToTimeInt(Timestamp.now())) {
                    timeList.add(it)
                }
            }
        }

        val events = when(val result = firebaseDataRepository.getAllEvents(userId)){
            is Result.Success -> result.data
            else -> listOf()
        }
        val eventList = events.filter {
            ItemArranger.isThatDayNeedToDo(ItemData(eventData = it), Timestamp.now())
        }
        eventList.forEach { event ->
            event.executedTime.forEach {
                if (getTimeStampToTimeInt(it) > getTimeStampToTimeInt(Timestamp.now())) {
                    timeList.add(it)
                }
            }
        }

        val cares = when(val result = firebaseDataRepository.getAllCares(userId)){
            is Result.Success -> result.data
            else -> listOf()
        }
        val careList = cares.filter {
            ItemArranger.isThatDayNeedToDo(ItemData(careData = it), Timestamp.now())
        }
        careList.forEach { care ->
            care.executedTime.forEach {
                if (getTimeStampToTimeInt(it) > getTimeStampToTimeInt(Timestamp.now())) {
                    timeList.add(it)
                }
            }
        }

        timeList.distinct()
        timeList.sort()

        applyToAlarmManager(timeList)
    }

    private fun applyToAlarmManager(list: List<Timestamp>) {
        val c = Calendar.getInstance()
        c.time = Timestamp.now().toDate()
        val d = c.get(Calendar.DATE)
        Log.i("startWork", "RebuildAlarm_applyToAlarmManager")
        list.forEach {
            val intent =
                Intent(PublicApplication.application.applicationContext, AlarmReceiver::class.java)

            c.time = it.toDate()
            val h = c.get(Calendar.HOUR_OF_DAY)
            val m = c.get(Calendar.MINUTE)

            intent.addCategory("$d$h$m")
            intent.putExtra("timeTag", getTimeStampToTimeInt(it))
            intent.action = POST_NOTIFICATION

            val pendingIntent = PendingIntent.getBroadcast(
                PublicApplication.application.applicationContext,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

            val alarmManager =
                PublicApplication.application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            c.set(Calendar.DATE, d)
            c.set(Calendar.HOUR_OF_DAY, h)
            c.set(Calendar.MINUTE, m)
            c.add(Calendar.MINUTE, -5)

            alarmManager.set(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
        }
    }
}
