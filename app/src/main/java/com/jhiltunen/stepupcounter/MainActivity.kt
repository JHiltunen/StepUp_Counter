package com.jhiltunen.stepupcounter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jhiltunen.stepupcounter.ui.userinfopopup.UserInfoPopup
import com.jhiltunen.stepupcounter.utils.Person
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import kotlinx.coroutines.InternalCoroutinesApi

class MainActivity : AppCompatActivity() {

    private var sharedPreferencesManager = SharedPreferencesManager()
    private var userFirstTime = true

    // request code for PHYSICAL_ACTIVITY
    // the code can be any number
    private val PHYSICAL_ACTIVITY = 100
    private val steps = Person()

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_analytics, R.id.navigation_information
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        userFirstTime = sharedPreferencesManager.loadFirstTime(applicationContext)

        if (userFirstTime) {
            userFirstTime = false
            sharedPreferencesManager.saveFirstTime(applicationContext, userFirstTime)

            val i = Intent(this, UserInfoPopup::class.java)
            startActivity(i)
            finish()
        }

        // Since API level 29 we are required to ask the user for ACTIVITY_RECOGNITION at runtime.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)!= PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            //ask for permission to use sensor if API level >= 29
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    PHYSICAL_ACTIVITY
                )
            }
        } else {
            // User has already given the permission
            Toast.makeText(this, "Permission is already provided", Toast.LENGTH_SHORT).show()
        }
    }
}