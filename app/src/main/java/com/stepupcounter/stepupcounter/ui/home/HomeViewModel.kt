package com.stepupcounter.stepupcounter.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    init {
        steps = sharedPreferencesManager.loadData(getApplication())
    }

    private fun dateBeforeCurrent(lastDate : Date, currentDate : Date): Boolean {
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
        if (steps.getPreviousSteps() == -1f) {
            steps.setpreviousStepsValue(totalStepsSinceLastRebootOfDevice)
            Log.d(TAG, "Asetetaan previousStepsValueksi sensorin arvo: $totalStepsSinceLastRebootOfDevice")
        }

        val currentSteps: Int

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

        val currentDate = Date()
        val lastDate : Date = sdf.parse(steps.getLastDate())

        // if last date saved to LinkedHashMap is before current date we need to reset steps count becauuse
        if (dateBeforeCurrent(lastDate, currentDate)) {
            Log.d(TAG, "Sama päivä")
            steps.addValue(sdf.format(Date()), currentSteps.toFloat())
            saveStepsToSharedPreferences()
        } else {
            stepsCount = 0
            steps.setpreviousStepsValue(totalStepsSinceLastRebootOfDevice)
            steps.addValue(sdf.format(Date()), 0f)
            // call saveData to save it to SharedPreferences
            saveStepsToSharedPreferences()
        }

        stepsCount = currentSteps
    }

    private fun saveStepsToSharedPreferences() {
        sharedPreferencesManager.saveData(getApplication(), steps)
    }
}