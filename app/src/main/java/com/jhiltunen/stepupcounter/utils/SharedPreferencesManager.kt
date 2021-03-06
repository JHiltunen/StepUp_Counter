package com.jhiltunen.stepupcounter.utils

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * Saves data to SharedPreferences.
 * Loads data from SharedPreferences.
 */
class SharedPreferencesManager {

    private val TAG = "SharedPreferencesManager"
    // set variables for preference names
    private val userPreference = "user"
    private val userPreferencesKey = "userId"
    private val firstLaunchPreference = "firstLaunch"
    private val firstLaunchPreferencesKey = "isFirstLaunch"
    private val totalStepsSinceLastRebootOfDeviceKey = "totalStepsSinceLastRebootOfDevice"

    /**
     * Function to load saved userId from shared preference.
     * @param context Application context.
     * @return Returns the userId that's saved to shared preference or -1 if there wasn't saved userId'
     */
    fun loadUserId(context: Context): Long {
        // get SharedPreferences
        val sharedPreferences = context.getSharedPreferences(userPreference, Context.MODE_PRIVATE)
        Log.d(TAG, "loadUserId: ${sharedPreferences.getLong(userPreferencesKey, -1)}")
        // return saved userId -> if not found, then return -1
        return sharedPreferences.getLong(userPreferencesKey, -1)
    }

    /**
     * Saves userId given in parameters to sharedPreferences.
     * @param context Application context
     * @param userId That's going to be saved to sharedPreferences.
     */
    fun saveUserId(context: Context, userId: Long) {
        Log.d("TAG", "saveId: $userId")
        // get shared preference
        val sp = context.getSharedPreferences(userPreference, AppCompatActivity.MODE_PRIVATE)
        // edit shared preferences
        sp.edit().apply{
            // put userId as long
            putLong(userPreferencesKey, userId)
            apply()
            // apply changes and save
        }
    }

    /**
     * Saves the parameter boolean value to shared preferences.
     * @param context Application context.
     * @param userFirstTime Boolean value that tells if user launches app for the first time
     */
    fun saveIsFirstLaunch(context: Context, userFirstTime: Boolean) {
        // get SharedPreferences
        val sp = context.getSharedPreferences(firstLaunchPreference, AppCompatActivity.MODE_PRIVATE)
        // edit
        sp.edit().apply{
            // put boolean value
            putBoolean(firstLaunchPreferencesKey, userFirstTime)
            // apply and save data
            apply()
        }
    }

    /**
     * Loads the isFirstLaunch boolean value from sharedPreferences.
     * @param context ApplicationContext.
     * @return Boolean that tells if it's app first launch.
     */
    fun loadIsFirstLaunch(context: Context): Boolean {
        // get SharedPreferences
        val sp = context.getSharedPreferences(firstLaunchPreference, AppCompatActivity.MODE_PRIVATE)
        // return boolean value
        return sp.getBoolean(firstLaunchPreferencesKey, true)
    }

    /**
     * Saves given value to shared preferences.
     * @param context Application context.
     * @param totalStepsSinceLastRebootOfDevice Number of steps since last reboot of device.
     */
    fun saveSensorValueToSharedPreferences(context: Context, totalStepsSinceLastRebootOfDevice: Float) {
        // get SharedPreferences
        val sp = context.getSharedPreferences(firstLaunchPreference, AppCompatActivity.MODE_PRIVATE)
        // edit
        sp.edit().apply{
            // put boolean value
            putFloat(totalStepsSinceLastRebootOfDeviceKey, totalStepsSinceLastRebootOfDevice)
            // apply and save data
            apply()
        }
    }

    /**
     * Loads totalStepsSinceLastRebootOfDevice from shared preferences.
     * @param context Application context.
     * @return totalStepsSinceLastRebootOfDevice Number of steps since last reboot of device.
     */
    fun loadTotalStepsSinceLastRebootOfDevice(context: Context): Float {
        // get SharedPreferences
        val sp = context.getSharedPreferences(firstLaunchPreference, AppCompatActivity.MODE_PRIVATE)
        // return boolean value
        return sp.getFloat(totalStepsSinceLastRebootOfDeviceKey, 0f)
    }
}