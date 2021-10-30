package com.weiting.tohealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.weiting.tohealth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = this.findNavController(R.id.myNavHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (navController.currentDestination?.id){
                R.id.itemEditFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.editNoteAndCalenderItemFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}