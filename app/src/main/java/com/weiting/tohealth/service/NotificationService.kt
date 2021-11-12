package com.weiting.tohealth.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
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
import kotlin.random.Random

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
        val groupLIdList = intent.getIntegerArrayListExtra("groupList") as MutableList<String>

        startListenChat(groupLIdList)
        startListenNotification(memberIdList)
        return super.onStartCommand(intent, flags, startId)
    }

    /*
        The Listener will be recreated or not? resolved
     */

    private val acceptedChatIdList = mutableListOf<String>()

    private fun startListenChat(group: List<String>) {
        firebaseDataRepository.getLiveChatMessageForService(
            Firebase.auth.currentUser?.uid!!,
            group
        ).observe(this){

            val chatList = it.filter {
                getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(Timestamp.now()) && it.id !in acceptedChatIdList
            }

            chatList.forEach {
                acceptedChatIdList.add(it.id!!)
                showLatestChat(it)
            }

        }
    }

    private fun startListenNotification(list: List<String>) {
        firebaseDataRepository.getLiveNotificationForService(list)
            .observe(this) {
//              Log.i("work", "startListener")
                val notificationList = it.filter {
                    getTimeStampToDateInt(it.createdTime!!) == getTimeStampToDateInt(Timestamp.now()) && !it.alreadySend.contains(
                        Firebase.auth.currentUser?.uid
                    )
                }

                notificationList.forEachIndexed { index, notification ->
                    showNotification(notification, index)
                    firebaseDataRepository.postOnGetNotificationForService(notification)
                }
            }
    }

    private fun showLatestChat(chat: Chat){
        val cNotification = NotificationCompat.Builder(this, "toHealth")
            .setSmallIcon(R.drawable.exclamation_mark)
            .setContentTitle(chat.creator)
            .setContentText(chat.context)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val id = Random.nextInt(10)
        notificationManager.notify(id, cNotification)
    }

    //TODO get the detail info.
    private fun showNotification(notification: Notification, int: Int) {

        val nNotification = NotificationCompat.Builder(this, "toHealth")
            .setSmallIcon(R.drawable.exclamation_mark)
            .setContentTitle(notification.id)
            .setContentText(notification.itemId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val id = Random.nextInt(10)
        notificationManager.notify(id, nNotification)
    }

}

