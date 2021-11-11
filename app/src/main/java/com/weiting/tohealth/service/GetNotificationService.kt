package com.weiting.tohealth.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.R
import com.weiting.tohealth.data.*
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.getTimeStampToDateInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GetNotificationService : LifecycleService() {

    lateinit var firebaseDataRepository: FirebaseRepository

    override fun onCreate() {
        super.onCreate()
        firebaseDataRepository = PublicApplication.application.firebaseDataRepository
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val memberIdList = intent?.getIntegerArrayListExtra("memberList") as MutableList<String>
        startListener(memberIdList)


        return super.onStartCommand(intent, flags, startId)
    }

    private fun startListener(list: List<String>) {
        Log.i("work", "startListener")
        firebaseDataRepository.getLiveNotification(list)
            .observe(this) {

                Toast.makeText(this.applicationContext, it.size.toString(), Toast.LENGTH_LONG)
                    .show()

                it.filter {
                    getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(Timestamp.now())
                }

                it.forEachIndexed { index, notification ->
                    showNotification(notification, index)
                }
            }
    }

    private fun showNotification(notification: Notification, int: Int) {
        val builder = NotificationCompat.Builder(this, "toHealth")
            .setSmallIcon(R.drawable.exclamation_mark)
            .setContentTitle(notification.id)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val name = "toHealth"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("toHealth", name, importance)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(int, builder)
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}