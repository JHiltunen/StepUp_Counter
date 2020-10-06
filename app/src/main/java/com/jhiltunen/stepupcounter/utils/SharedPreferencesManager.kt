package com.jhiltunen.stepupcounter.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class SharedPreferencesManager {

    private val PREFERENECE_NAME = "USER"
    private val PREFERENCE_KEY = "steps"
    private val TAG = "SharedPreferencesManager"

    fun loadUserId(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        Log.d(TAG, "loadUserId: ${sharedPreferences.getLong("userId", -1)}")
        return sharedPreferences.getLong("userId", -1)
    }

    fun saveIsFirstLaunch(context: Context, userFirstTime: Boolean) {
        val sp = context.getSharedPreferences("FIRST_TIME", AppCompatActivity.MODE_PRIVATE)
        sp.edit().apply{
            putBoolean("BOOLEAN_FIRST_TIME", userFirstTime)
            apply()
        }
    }

    fun loadIsFirstLaunch(context: Context): Boolean {
        val sp = context.getSharedPreferences("FIRST_TIME", AppCompatActivity.MODE_PRIVATE)
        return sp.getBoolean("BOOLEAN_FIRST_TIME", true)
    }
}