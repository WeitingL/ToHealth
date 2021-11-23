package com.weiting.tohealth.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.weiting.tohealth.works.PostUnCheckedLogsWork
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.works.RebuildAlarm
import com.weiting.tohealth.works.TodoListNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val database = PublicApplication.application.firebaseDataRepository

    override fun onReceive(p0: Context, p1: Intent) {

        Log.i("startWork", "onReceive: ${p1.action}")

        coroutineScope.launch {
            when {

                //23:59:00
                p1.action?.equals("check_today_unChecked_logs") == true -> {
                    PostUnCheckedLogsWork().checkTodayUnCheckedLogs(database)
                }

                p1.action?.equals("item_notification") == true -> {
                    val timeTag = p1.getIntExtra("timeTag", 0)
                    TodoListNotification().receiveIntentFromAlarm(timeTag, database)
                }
            }
        }
    }
}