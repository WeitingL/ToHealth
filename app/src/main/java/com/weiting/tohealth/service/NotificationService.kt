package com.weiting.tohealth.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.*
import com.weiting.tohealth.data.*
import com.weiting.tohealth.util.Util.toNotificationTextForMeasureLog
import com.weiting.tohealth.util.Util.toUnit
import kotlin.random.Random
import kotlinx.coroutines.*

const val APP_NAME = "toHealth"

class NotificationService : LifecycleService() {

    // foreground service
    lateinit var firebaseDataRepository: FirebaseRepository
    lateinit var notificationManager: NotificationManager

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val memberListeningList = mutableListOf<String>()
    private val groupIdListeningList = mutableListOf<String>()

    private val userId = Firebase.auth.currentUser?.uid?:""

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
            .setContentTitle(getString(R.string.foreNotificationTitle))
            .setContentText(getString(R.string.touchNotificationHint))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        startForeground(99, foregroundNotification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        firebaseDataRepository.getLiveUser(userId).observe(this) {
            if (it.groupList.isNotEmpty()) {
                it.groupList.forEach { groupId ->
                    if (groupId !in groupIdListeningList) {
                        startListenChat(groupId)
                    }
                    startListenMembers(groupId)
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startListenMembers(groupId: String) {
        firebaseDataRepository.getLiveMembers(groupId).observe(this@NotificationService) {
            it.forEach { member ->
                if (member.userId !in memberListeningList) {
                    memberListeningList.add(member.userId ?: "")
                    startListenAlertMessage(member.userId ?: "")
                }
            }
        }
    }

    private fun startListenChat(groupId: String) {
        firebaseDataRepository.getLiveChatMessagesForService(groupId).observe(this) {
            it.forEach { chat ->
                if (Firebase.auth.currentUser?.uid !in chat.isReadList) {
                    showLatestChat(chat)
                    firebaseDataRepository.postOnGetChatForService(chat)
                }
            }
        }
    }

    private fun startListenAlertMessage(userId: String) {
        firebaseDataRepository.getLiveAlertMessages(listOf(userId))
            .observe(this) {

                val alertMessageList = it.filter { message ->
                    !message.alreadySend.contains(
                        Firebase.auth.currentUser?.uid
                    )
                }

                alertMessageList.forEach { notification ->
                    showNotification(notification)
                    firebaseDataRepository.postOnGetAlertMessagesForService(notification)
                }
            }
    }

    private fun showLatestChat(chat: Chat) {
        coroutineScope.launch {
            val group = when (val result = firebaseDataRepository.getGroups(chat.groupId ?: "")) {
                is Result.Success -> result.data
                else -> null
            }
            val groupName = group?.first()?.groupName

            val user = when (val result = firebaseDataRepository.getUser(chat.creator ?: "")) {
                is Result.Success -> result.data
                else -> null
            }
            val userName = user?.name

            val chatNotification = RemoteViews(packageName, R.layout.altermessage_chat)
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

    private fun showNotification(alertMessage: AlertMessage) {
        coroutineScope.launch {
            val user =
                when (val result = firebaseDataRepository.getUser(alertMessage.userId ?: "")) {
                    is Result.Success -> result.data
                    else -> null
                }
            val userName = user?.name

            val alertNotification = RemoteViews(packageName, R.layout.altermessage_chat)
            when (alertMessage.result) {
                4 -> {
                    val measure = when (val result =
                        firebaseDataRepository.getMeasure(alertMessage.itemId ?: "")) {
                        is Result.Success -> result.data
                        else -> null
                    }
                    val measureLog = when (val result = firebaseDataRepository.getMeasureLog(
                        alertMessage.itemId ?: "",
                        alertMessage.logId ?: ""
                    )) {
                        is Result.Success -> result.data
                        else -> null
                    }

                    alertNotification.setTextViewText(R.id.tv_name, "測量項目異常 - $userName")
                    alertNotification.setTextViewText(
                        R.id.tv_content,
                        toNotificationTextForMeasureLog(
                            measure ?: Measure(),
                            measureLog ?: MeasureLog()
                        )
                    )
                }

                5 -> {
                    alertNotification.setTextViewText(R.id.tv_name, "多次未完成 - $userName")
                    alertNotification.setTextViewText(R.id.tv_content, "昨天多次未完成事項")
                }

                6 -> {
                    val drugItem =
                        when (val result = firebaseDataRepository.getDrug(alertMessage.itemId!!)) {
                            is Result.Success -> result.data
                            else -> null
                        }
                    alertNotification.setTextViewText(R.id.tv_name, "藥物快要用完了 - $userName")
                    alertNotification.setTextViewText(
                        R.id.tv_content,
                        "剩餘服藥次數: ${drugItem?.stock ?: 0F / (drugItem?.dose ?: 0F)} 次" +
                                "\n剩餘量: ${drugItem?.stock} ${toUnit(drugItem?.unit)} "
                    )
                }
            }

            val Notification = NotificationCompat.Builder(this@NotificationService, APP_NAME)
                .setSmallIcon(R.drawable.exclamation_mark)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContent(alertNotification)
                .build()

            val id = Random.nextInt(15)
            notificationManager.notify(id, Notification)
        }
    }
}
