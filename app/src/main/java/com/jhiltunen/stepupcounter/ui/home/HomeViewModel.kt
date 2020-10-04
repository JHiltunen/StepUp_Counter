package com.jhiltunen.stepupcounter.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jhiltunen.stepupcounter.data.database.HealthDatabase
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.logic.dao.HealthDao
import com.jhiltunen.stepupcounter.logic.repository.HealthRepository
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import com.jhiltunen.stepupcounter.utils.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.Days
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

@InternalCoroutinesApi
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HealthRepository
    val getAllSteps: LiveData<List<Steps>>

    init {
        val healthDao = HealthDatabase.getDatabase(application).healthDao()
        repository = HealthRepository(healthDao)

        getAllSteps = repository.getAllUsersSteps
        println(getAllSteps.toString())
    }

    fun addSteps(steps: Steps) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSteps(steps)
        }
    }

    private val TAG = "HomeViewModel"
    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    private var sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    private var person : Person = Person()

    var bmi = 0.0

    var stepsCount = 0

    fun updateStepsCount() {
        person = sharedPreferencesManager.loadData(getApplication())
        Log.d("stepscount","Askeleet tältä päivältä:" + person.getStepsFromSpecificDate(sdf.format(Date())))
        stepsCount = person.getStepsFromSpecificDate(sdf.format(Date())).toInt()
    }

    fun calculateBodyMassIndex() {
        person = sharedPreferencesManager.loadData(getApplication())
        bmi = person.getWeight() / (person.getHeight() / 100.0).pow(2)
        println("BMI: " + bmi.toString())
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
        val lastDate : Date = sdf.parse(person.getLastDate())

        var currentSteps: Int = person.getStepsFromSpecificDate(sdf.format(Date())).toInt()

        if (!dateIsSameAsCurrentDate(lastDate, currentDate)) {
            stepsCount = person.getStepsFromSpecificDate(sdf.format(Date())).toInt()
            person.addValue(sdf.format(Date()), 0f)
            // call saveData to save it to SharedPreferences
            saveStepsToSharedPreferences()
        } else {
            if (totalStepsSinceLastRebootOfDevice == 0f) {
                Log.d(TAG, "Sensorin arvo on nolla")
                // in case where sensor value is zero
                currentSteps = person.getStepsFromSpecificDate(sdf.format(Date())).toInt()
                person.setpreviousStepsValue(currentSteps.toFloat())
            } else {
                if (totalStepsSinceLastRebootOfDevice > person.getStepsFromSpecificDate(sdf.format(Date()))) {
                    Log.d(TAG, "totalStepsSinceLastRebootOfDevice > person.getStepsFromSpecificDate(sdf.format(Date()))")
                    currentSteps = totalStepsSinceLastRebootOfDevice.toInt() - person.getPreviousSteps().toInt()

                } else {
                    Log.d(TAG, "ELSE")
                    Log.d(TAG, "ELSE haara")
                    currentSteps = person.getPreviousSteps().toInt() + totalStepsSinceLastRebootOfDevice.toInt()
                }
            }

            Log.d(TAG, "Sama päivä")
            person.addValue(sdf.format(Date()), currentSteps.toFloat())
            saveStepsToSharedPreferences()
        }

        stepsCount = currentSteps
    }

    private fun saveStepsToSharedPreferences() {
        sharedPreferencesManager.saveData(getApplication(), person)
    }

    fun addNewDefaultValueIfDateHasChanged() {
        if (!dateIsSameAsCurrentDate(sdf.parse(person.getLastDate()), Date())) {
            person.addValue(sdf.format(Date()), 0f)
        }
    }
}