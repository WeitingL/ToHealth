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
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.getTimeStampToTimeInt
import com.weiting.tohealth.receiver.AlarmReceiver
import com.weiting.tohealth.util.ItemArranger
import java.sql.Time
import java.util.*

class RebuildAlarm() {

    /*
        Only apply the alarm with timeStamp
     */

    suspend fun updateNewTodoListToAlarmManager(firebaseDataRepository: FirebaseRepository) {
        val userId = Firebase.auth.currentUser?.uid
        val timeList = mutableListOf<Timestamp>()

        val drugList = firebaseDataRepository.getAllDrugs(userId!!).filter {
            ItemArranger().isTodayNeedToDo(ItemType.DRUG, ItemData(DrugData = it))
        }
        drugList.forEach {
            it.executedTime.forEach {
                timeList.add(it)
            }
        }

        val measureLog = firebaseDataRepository.getAllMeasures(userId).filter {
            ItemArranger().isTodayNeedToDo(ItemType.MEASURE, ItemData(MeasureData = it))
        }
        measureLog.forEach {
            it.executedTime.forEach {
                timeList.add(it)
            }
        }

        val activityList = firebaseDataRepository.getAllActivities(userId).filter {
            ItemArranger().isTodayNeedToDo(ItemType.ACTIVITY, ItemData(ActivityData = it))
        }
        activityList.forEach {
            it.executedTime.forEach {
                timeList.add(it)
            }
        }

        val careList = firebaseDataRepository.getAllCares(userId).filter {
            ItemArranger().isTodayNeedToDo(ItemType.CARE, ItemData(CareData = it))
        }
        careList.forEach {
            it.executeTime.forEach {
                timeList.add(it)
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
        Log.i("startWork", "applyToAlarmManager")
        list.forEach {
            val intent =
                Intent(PublicApplication.application.applicationContext, AlarmReceiver::class.java)

            c.time = it.toDate()
            val h = c.get(Calendar.HOUR_OF_DAY)
            val m = c.get(Calendar.MINUTE)

            intent.addCategory("$d$h$m")
            intent.putExtra("timeTag", getTimeStampToTimeInt(it))
            intent.action = "item_notification"

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