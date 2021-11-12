package com.weiting.tohealth.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.R
import com.weiting.tohealth.data.*
import com.weiting.tohealth.getTimeStampToDateInt

class NotificationService : LifecycleService() {

    lateinit var firebaseDataRepository: FirebaseRepository
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()

        firebaseDataRepository = PublicApplication.application.firebaseDataRepository
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "toHealthService",
            "toHealthService",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notificationLayout = RemoteViews(packageName, R.layout.notification_flag)
        val foregroundNotification = NotificationCompat.Builder(this, "toHealthService")
            .setSmallIcon(R.drawable.hospital_sign)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setColorized(true)
            .setColor(getColor(R.color.areo_blue))
            .setCustomContentView(notificationLayout)
            .build()

        startForeground(99, foregroundNotification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val memberIdList = intent?.getIntegerArrayListExtra("memberList") as MutableList<String>

        startListenNotification(memberIdList)
        return super.onStartCommand(intent, flags, startId)
    }

    /*
        The Listener will be recreated or not?
     */


    private fun startListenChat(list: List<String>) {


    }

    private fun startListenNotification(list: List<String>) {
        firebaseDataRepository.getLiveNotificationForService(list)
            .observe(this) {
//              Log.i("work", "startListener")
                val list = it.filter {
                    getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(Timestamp.now()) && !it.alreadySend.contains(
                        Firebase.auth.currentUser?.uid
                    )
                }

                list.forEachIndexed { index, notification ->
                    showNotification(notification, index)
                    firebaseDataRepository.postOnGetNotificationForService(notification)
                }
            }
    }

    //TODO get the detail info.
    private fun showNotification(notification: Notification, int: Int) {
        val builder = NotificationCompat.Builder(this, "toHealth")
            .setSmallIcon(R.drawable.exclamation_mark)
            .setContentTitle(notification.id)
            .setContentText(notification.itemId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(int, builder)
    }

}

