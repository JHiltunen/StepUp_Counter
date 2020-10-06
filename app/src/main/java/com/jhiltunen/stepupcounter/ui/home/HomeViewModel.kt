package com.jhiltunen.stepupcounter.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jhiltunen.stepupcounter.data.database.HealthDatabase
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.logic.repository.HealthRepository
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.Days
import java.text.SimpleDateFormat
import java.util.*

@InternalCoroutinesApi
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "HomeViewModel"
    private var sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    private val repository: HealthRepository
    val getUser: LiveData<User>
    val getUsersStepsCountFromSpecificDate: LiveData<Int>

    init {
        val healthDao = HealthDatabase.getDatabase(application).healthDao()
        repository = HealthRepository(healthDao, sharedPreferencesManager.loadUserId(getApplication<Application>().applicationContext))
        getUser = repository.getUser
        getUsersStepsCountFromSpecificDate = repository.getUsersStepsCountFromSpecificDate(sdf.format(Date()))
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

    fun calculateStepsDatabase(totalStepsSinceLastRebootOfDevice: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentDate = Date()
            val lastDate : Date = repository.getUsersLastSavedDate()

            var steps : Steps = repository.getUsersStepsFromSpecificDate(sdf.format(currentDate))

            if (steps == null || !dateIsSameAsCurrentDate(lastDate, currentDate)) {
                repository.addSteps(Steps(0, sdf.format(currentDate), 0, totalStepsSinceLastRebootOfDevice.toInt(), sharedPreferencesManager.loadUserId(getApplication<Application>().applicationContext)))
            } else {
                steps = repository.getUsersStepsFromSpecificDate(sdf.format(currentDate))

                val usersStepsFromToday = steps.value

                var currentSteps: Int

                if (totalStepsSinceLastRebootOfDevice == 0f) {
                    Log.d(TAG, "Sensorin arvo on nolla")
                    // in case where sensor value is zero
                    currentSteps = usersStepsFromToday
                    repository.updateSteps(Steps(
                        id = steps.id,
                        date = steps.date,
                        value = steps.value,
                        previousSteps = currentSteps,
                        userId = steps.userId
                    ))
                } else {
                    if (totalStepsSinceLastRebootOfDevice > usersStepsFromToday) {
                        currentSteps = totalStepsSinceLastRebootOfDevice.toInt() - steps.previousSteps

                    } else {
                        Log.d(TAG, "ELSE")
                        Log.d(TAG, "ELSE haara")
                        currentSteps = steps.previousSteps + totalStepsSinceLastRebootOfDevice.toInt()
                    }
                }

                Log.d(TAG, "Sama päivä")
                repository.updateSteps(Steps(steps.id, steps.date, currentSteps, steps.previousSteps, steps.userId))
            }
        }
    }
}