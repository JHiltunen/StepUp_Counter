package com.stepupcounter.stepupcounter.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

class SharedPreferencesManager {

    private val PREFERENECE_NAME = "healthData"
    private val PREFERENCE_KEY = "steps"
    private val TAG = "SharedPreferencesManager"

    private val gson = Gson()

    /**
     * Saves the number of steps in the shared preferences with the key "steps"
     * Name of sharedPreferences is "myPrefs"
     */
    fun saveData(context: Context, steps: Steps) {
        Log.d(TAG,
            "Saving data to sharedPreferences: SharedPreference name -> $PREFERENECE_NAME with key -> $PREFERENCE_KEY"
        )
        val sharedPreferences : SharedPreferences = context.getSharedPreferences(PREFERENECE_NAME, Context.MODE_PRIVATE)

        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(PREFERENCE_KEY, convertObjectToJson(steps))
        editor.apply()
    }

    /**
     * Loads the number of steps saved to shared preferences with the key "key1"
     * Name of sharedPreferences is "myPrefs"
     */
    fun loadData(context: Context): Steps {
        // loads steps count from shared preferences
        val sharedPreferences : SharedPreferences = context.getSharedPreferences(
            PREFERENECE_NAME,
            Context.MODE_PRIVATE
        )

        return convertJsonToStepsObject(sharedPreferences.getString(PREFERENCE_KEY, ""))
    }

    private fun convertObjectToJson(steps: Steps): String {
        Log.d(TAG, "Converting Steps object: ${steps.toString()} to Json")
        return gson.toJson(steps)
    }

    private fun convertJsonToStepsObject(json: String?): Steps {
        if (!json.isNullOrEmpty()) {
            return gson.fromJson(json, Steps::class.java)
        }
        return Steps()
    }
}