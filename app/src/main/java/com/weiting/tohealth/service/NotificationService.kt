package com.weiting.tohealth.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.*
import com.weiting.tohealth.data.*
import kotlinx.coroutines.*
import kotlin.random.Random

const val APP_NAME = "toHealth"

class NotificationService : LifecycleService() {

    //foreground service
    lateinit var firebaseDataRepository: FirebaseRepository
    lateinit var notificationManager: NotificationManager

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        firebaseDataRepository = PublicApplication.application.firebaseDataRepository
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            APP_NAME,
            APP_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val mainIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val foregroundNotification = NotificationCompat.Builder(this, APP_NAME)
            .setSmallIcon(R.drawable.ic_tohealth)
            .setContentTitle("ToHealth 正在監控您的健康數據與群組訊息")
            .setContentText("輕觸可以開啟應用程式")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        startForeground(99, foregroundNotification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        firebaseDataRepository.getLiveUser(UserManager.UserInformation.id!!).observe(this) {
            if (it.groupList.isNotEmpty()) {
                startListenChat(it.groupList)
                getGroupsMembers(it.groupList)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun getGroupsMembers(groupList: List<String>) {
        val memberList = MutableLiveData<MutableList<String>>().apply {
            groupList.forEach {
                firebaseDataRepository.getLiveMember(it).observe(this@NotificationService) {
                    it.forEach {
                        value?.add(it.id!!)
                    }
                }
            }
        }
        memberList.observe(this) {
            if (it.isNotEmpty()) {
                startListenNotification(it)
            }
        }
    }


    /*
        The Listener will be recreated or not? resolved!!
     */

    private fun startListenChat(groupList: List<String>) {
        firebaseDataRepository.getLiveChatMessageForService(
            Firebase.auth.currentUser?.uid!!,
            groupList
        ).observe(this) {
            it.forEach {
                if (Firebase.auth.currentUser?.uid !in it.isReadList) {
                    showLatestChat(it)
                    firebaseDataRepository.postOnGetChatForService(it)
                }
            }
        }
    }

    private fun startListenNotification(list: List<String>) {
        firebaseDataRepository.getLiveNotificationForService(list)
            .observe(this) {
//              Log.i("work", "startListener")

                val notificationList = it.filter {
                    !it.alreadySend.contains(
                        Firebase.auth.currentUser?.uid
                    )
                }

                notificationList.forEachIndexed { index, notification ->
                    showNotification(notification, index)
                    firebaseDataRepository.postOnGetNotificationForService(notification)
                }
            }
    }

    private fun showLatestChat(chat: Chat) {
        coroutineScope.launch {
            val groupName = firebaseDataRepository.getGroups(chat.groupId!!).first().groupName
            val userName = firebaseDataRepository.getUser(chat.creator!!).name

            val chatNotification = RemoteViews(packageName, R.layout.notification_chat)
            chatNotification.setTextViewText(R.id.tv_name, "群組: $groupName")
            chatNotification.setTextViewText(R.id.tv_content, "$userName: ${chat.context}")


            val cNotification = NotificationCompat.Builder(this@NotificationService, APP_NAME)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setSmallIcon(R.drawable.user_1)
                .setOnlyAlertOnce(true)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContent(chatNotification)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            val id = Random.nextInt(15)
            notificationManager.notify(id, cNotification)
        }
    }

    private fun showNotification(notification: Notification, int: Int) {
        coroutineScope.launch {
            val userName = firebaseDataRepository.getUser(notification.userId!!).name

            val alertNotification = RemoteViews(packageName, R.layout.notification_chat)
            when (notification.result) {
                4 -> {
                    val measure = firebaseDataRepository.getMeasure(notification.itemId!!)
                    val measureLog = firebaseDataRepository.getMeasureLog(
                        notification.itemId,
                        notification.logId!!
                    )

                    alertNotification.setTextViewText(R.id.tv_name, "測量項目異常 - $userName")
                    alertNotification.setTextViewText(
                        R.id.tv_content,
                        toNotificationTextForMeasureLog(measure, measureLog)
                    )
                }

                5 -> {
                    alertNotification.setTextViewText(R.id.tv_name, "多次未完成 - $userName")
                    alertNotification.setTextViewText(R.id.tv_content, "昨天多次未完成事項")
                }

                6 -> {
                    val drugItem = firebaseDataRepository.getDrug(notification.itemId!!)
                    alertNotification.setTextViewText(R.id.tv_name, "藥物快要用完了 - $userName")
                    alertNotification.setTextViewText(
                        R.id.tv_content,
                        "剩餘服藥次數: ${drugItem.stock / drugItem.dose} 次" +
                                "\n剩餘量: ${drugItem.stock} ${toUnit(drugItem.unit)} "
                    )
                }
            }

            val nNotification = NotificationCompat.Builder(this@NotificationService, APP_NAME)
                .setSmallIcon(R.drawable.exclamation_mark)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContent(alertNotification)
                .build()

            val id = Random.nextInt(15)
            notificationManager.notify(id, nNotification)
        }
    }


}

