package com.weiting.tohealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.weiting.tohealth.databinding.ActivityMainBinding
import com.weiting.tohealth.factory.MainActivityViewModelFactory
import com.weiting.tohealth.service.GetNotificationService
import io.grpc.internal.JsonUtil
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory =
            MainActivityViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)

//        viewModel.memberIdList.observe(this) {
//            val notificationIntent = Intent(this, GetNotificationService::class.java)
//            notificationIntent.putExtra("memberList", it as Serializable)
//            startService(notificationIntent)
//        }


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
}