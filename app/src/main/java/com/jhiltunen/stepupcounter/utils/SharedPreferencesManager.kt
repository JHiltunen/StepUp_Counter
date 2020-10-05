package com.jhiltunen.stepupcounter.utils

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
     * Saves the number of person in the shared preferences with the key "person"
     * Name of sharedPreferences is "myPrefs"
     */
    fun saveData(context: Context, person: Person) {
        Log.d(TAG,
            "Saving data to sharedPreferences: SharedPreference name -> $PREFERENECE_NAME with key -> $PREFERENCE_KEY"
        )
        val sharedPreferences : SharedPreferences = context.getSharedPreferences(PREFERENECE_NAME, Context.MODE_PRIVATE)

        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(PREFERENCE_KEY, convertObjectToJson(person))
        editor.apply()
    }

    /**
     * Loads the number of steps saved to shared preferences with the key "key1"
     * Name of sharedPreferences is "myPrefs"
     */
    fun loadData(context: Context): Person {
        // loads steps count from shared preferences
        val sharedPreferences : SharedPreferences = context.getSharedPreferences(
            PREFERENECE_NAME,
            Context.MODE_PRIVATE
        )

        return convertJsonToStepsObject(sharedPreferences.getString(PREFERENCE_KEY, ""))
    }

    fun loadUserId(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        Log.d(TAG, "loadUserId: ${sharedPreferences.getLong("userId", -1)}")
        return sharedPreferences.getLong("userId", -1)
    }

    private fun convertObjectToJson(person: Person): String {
        Log.d(TAG, "Converting Person object: ${person.toString()} to Json")
        return gson.toJson(person)
    }

    private fun convertJsonToStepsObject(json: String?): Person {
        if (!json.isNullOrEmpty()) {
            return gson.fromJson(json, Person::class.java)
        }
        return Person()
    }
}