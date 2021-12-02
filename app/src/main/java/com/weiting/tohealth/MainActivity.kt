package com.weiting.tohealth

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.weiting.tohealth.NavigationDestination.*
import com.weiting.tohealth.databinding.ActivityMainBinding
import com.weiting.tohealth.factory.MainActivityViewModelFactory
import com.weiting.tohealth.receiver.AlarmReceiver
import com.weiting.tohealth.receiver.CHECK_UNCHECKED_LOG
import com.weiting.tohealth.service.NotificationService
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory =
            MainActivityViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val notificationIntent = Intent(this, NotificationService::class.java)
        viewModel.isLogin.observe(this) {
            if (it) {
                viewModel.startSetAlarmForTodoList()
                setAlarmManagerToCheckLogs()
                stopService(notificationIntent)
                startForegroundService(notificationIntent)
            }
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.imMoreNotification.setOnClickListener {
            navController.navigate(NavigationDirections.actionGlobalNotificationFragment(viewModel.memberList.toTypedArray()))
        }

        navController.addOnDestinationChangedListener { _, _, _ ->
            viewModel.getNavigationDestination(
                when (navController.currentDestination?.id) {
                    R.id.groupFragment -> GroupFragment
                    R.id.loginFragment -> LoginFragment
                    R.id.homeFragment -> HomeFragment
                    R.id.fastAddFragment -> FastAddFragment
                    R.id.myGroupFragment -> MyGroupFragment
                    R.id.myStatisticFragment -> MyStatisticFragment
                    R.id.myManageFragment -> MyManageFragment
                    R.id.itemEditFragment -> ItemEditFragment
                    R.id.itemUpdateFragment -> ItemUpdateFragment
                    R.id.editNoteAndCalenderItemFragment -> EditNoteAndReminderFragment
                    R.id.measureRecordFragment -> MeasureRecordFragment
                    R.id.careRecordFragment -> CareRecordFragment
                    R.id.groupMemberStatisticFragment -> GroupMemberStatisticFragment
                    R.id.groupMemberManageFragment -> GroupMemberMenageFragment
                    R.id.notificationFragment -> NotificationFragment
                    else -> OtherFragment
                }
            )
        }
    }

    private fun setAlarmManagerToCheckLogs() {

        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        val time = c.timeInMillis
        Log.i("startWork", "setAlarmManagerToCheckLogs: ${c.time}")

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = CHECK_UNCHECKED_LOG

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }
}
