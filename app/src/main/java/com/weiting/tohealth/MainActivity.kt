package com.weiting.tohealth

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
import com.google.firebase.auth.FirebaseAuth
import com.weiting.tohealth.databinding.ActivityMainBinding
import com.weiting.tohealth.factory.MainActivityViewModelFactory
import com.weiting.tohealth.service.NotificationService
import java.io.Serializable
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var notificationServiceBinder: NotificationService.NotificationServiceBinder
    private var isServiceCreated by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory =
            MainActivityViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)

        val notificationIntent = Intent(this, NotificationService::class.java)
        bindService(notificationIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        viewModel.memberIdList.observe(this) {

            Log.i("isServiceCreated", isServiceCreated.toString())

            if (!isServiceCreated){
                notificationIntent.putExtra("memberList", it as Serializable)
                startService(notificationIntent)
            }
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


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val notificationServiceBinder = p1 as NotificationService.NotificationServiceBinder
            isServiceCreated = notificationServiceBinder.getServiceStatus()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {}

    }


}