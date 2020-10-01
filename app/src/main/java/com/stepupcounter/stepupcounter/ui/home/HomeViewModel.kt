package com.stepupcounter.stepupcounter.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stepupcounter.stepupcounter.utils.SharedPreferencesManager
import com.stepupcounter.stepupcounter.utils.Steps
import org.joda.time.DateTime
import org.joda.time.Days
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "HomeViewModel"
    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    private var sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    private var steps : Steps = Steps()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    var stepsCount = 0

    fun updateStepsCount() {
        steps = sharedPreferencesManager.loadData(getApplication())
        Log.d("stepscount","Askeleet tältä päivältä:" + steps.getStepsFromSpecificDate(sdf.format(Date())))
        stepsCount = steps.getStepsFromSpecificDate(sdf.format(Date())).toInt()
    }

    private fun dateIsSameAsCurrentDate(lastDate : Date, currentDate : Date): Boolean {
        val currentDateTime = DateTime(currentDate)
        val lastDateTime = DateTime(lastDate)

        // Joda
        Log.d(
            TAG,
            "currentDate: " + currentDate + "; lastSavedDate: " + lastDate + " date.before(currentdate): " + lastDate.before(
                currentDate
            )
        )
        return Days.daysBetween(lastDateTime, currentDateTime).days == 0
    }

    fun calculateSteps(totalStepsSinceLastRebootOfDevice : Float) {
        val currentDate = Date()
        val lastDate : Date = sdf.parse(steps.getLastDate())

        var currentSteps: Int = steps.getStepsFromSpecificDate(sdf.format(Date())).toInt()

        if (!dateIsSameAsCurrentDate(lastDate, currentDate)) {
            stepsCount = steps.getStepsFromSpecificDate(sdf.format(Date())).toInt()
            steps.addValue(sdf.format(Date()), 0f)
            // call saveData to save it to SharedPreferences
            saveStepsToSharedPreferences()
        } else {
            if (totalStepsSinceLastRebootOfDevice == 0f) {
                Log.d(TAG, "Sensorin arvo on nolla")
                // in case where sensor value is zero
                currentSteps = steps.getStepsFromSpecificDate(sdf.format(Date())).toInt()
                steps.setpreviousStepsValue(currentSteps.toFloat())
            } else {
                if (totalStepsSinceLastRebootOfDevice > steps.getStepsFromSpecificDate(sdf.format(Date()))) {
                    Log.d(TAG, "totalStepsSinceLastRebootOfDevice > steps.getStepsFromSpecificDate(sdf.format(Date()))")
                    currentSteps = totalStepsSinceLastRebootOfDevice.toInt() - steps.getPreviousSteps().toInt()

                } else {
                    Log.d(TAG, "ELSE")
                    Log.d(TAG, "ELSE haara")
                    currentSteps = steps.getPreviousSteps().toInt() + totalStepsSinceLastRebootOfDevice.toInt()
                }
            }

            Log.d(TAG, "Sama päivä")
            steps.addValue(sdf.format(Date()), currentSteps.toFloat())
            saveStepsToSharedPreferences()
        }

        stepsCount = currentSteps
    }

    private fun saveStepsToSharedPreferences() {
        sharedPreferencesManager.saveData(getApplication(), steps)
    }

    fun addNewDefaultValueIfDateHasChanged() {
        if (!dateIsSameAsCurrentDate(sdf.parse(steps.getLastDate()), Date())) {
            steps.addValue(sdf.format(Date()), 0f)
        }
    }
}