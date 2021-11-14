package com.weiting.tohealth

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.weiting.tohealth.databinding.ActivityMainBinding
import com.weiting.tohealth.factory.MainActivityViewModelFactory
import com.weiting.tohealth.receiver.AlarmReceiver
import com.weiting.tohealth.service.NotificationService
import java.io.Serializable
import java.sql.Time
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory =
            MainActivityViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)
        val notificationIntent = Intent(this, NotificationService::class.java)

        viewModel.memberIdList.observe(this) {
            stopService(notificationIntent)
            notificationIntent
                .putExtra("memberList", it as Serializable)
                .putExtra("groupList", viewModel.groupList as Serializable)
            startForegroundService(notificationIntent)
            setAlarmManagerToCheckLogs()
            setAlarmManagerToRearrangeItem()
        }

        val navController = this.findNavController(R.id.myNavHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (navController.currentDestination?.id) {
                R.id.loginFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE
                }
                R.id.itemEditFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.toolbar.visibility = View.VISIBLE
                }
                R.id.editNoteAndCalenderItemFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.toolbar.visibility = View.VISIBLE
                }
                R.id.groupMemberManageFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE
                }
                R.id.groupMemberStatisticFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setAlarmManagerToRearrangeItem(){
        Log.i("startWork", "setAlarmManagerToRearrangeItem")
        val c = Calendar.getInstance()
        c.time = Timestamp.now().toDate()
        c.add(Calendar.DAY_OF_YEAR, 1)
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 2)
        val time = c.timeInMillis

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "check_today_unChecked_logs"

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun setAlarmManagerToCheckLogs(){
        Log.i("startWork", "setAlarmManagerToCheckLogs")
        val c = Calendar.getInstance()
        c.time = Timestamp.now().toDate()
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 0)
        val time = c.timeInMillis

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "rebuild_plan"

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)

    }


}