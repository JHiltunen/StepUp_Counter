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
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.joda.time.Days
import java.text.SimpleDateFormat
import java.util.*

/**
 * Represents view model for HomeFragment.
 * Contains HomeFragment calculation logic.
 */
@InternalCoroutinesApi
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "HomeViewModel"
    private var sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    private val repository: HealthRepository
    // LiveData is used to observe changes to user information and users steps count
    val getUser: LiveData<User>
    val getUsersStepsCountFromSpecificDate: LiveData<Int>

    /**
     * Initialize view model and required variables
     */
    init {
        // create new Health Data Access Object
        val healthDao = HealthDatabase.getDatabase(application).healthDao()
        // initialize new repository and load userId from shared preferences
        repository = HealthRepository(healthDao, sharedPreferencesManager.loadUserId(getApplication<Application>().applicationContext))
        // get users information as LiveData (can be observed) from database
        getUser = repository.getUser
        // get users steps count as LiveData (can be observed) from specific date
        Log.d(TAG, ": ${repository.getUsersStepsCountFromSpecificDate("2020-10-07")}")
        getUsersStepsCountFromSpecificDate = repository.getUsersStepsCountFromSpecificDate("2020-10-07")
        viewModelScope.launch(Dispatchers.IO) {}
    }

    /**
     * Function to check if two last saved Date and current date is same day.
     * @param lastDate Date object that's last saved to database
     * @param currentDate Date object with value of current date.
     * @return Boolean This returns true if there's 0 days between the lastDate and currentDate objects. Else return false
     */
    private fun lastDateIsSameAsCurrentDate(lastDate : Date, currentDate : Date): Boolean {
        // convert java Date to Joda DateTime
        val currentDateTime = DateTime(currentDate)
        val lastDateTime = DateTime(lastDate)
        Log.d(
            TAG,
            "currentDate: " + currentDate + "; lastSavedDate: " + lastDate + " date.before(currentdate): " + lastDate.before(
                currentDate
            )
        )
        return Days.daysBetween(lastDateTime, currentDateTime).days == 0
    }

    /**
     * Function to calculate users current steps.
     * @param totalStepsSinceLastRebootOfDevice Float value from device step sensor
     */
    fun calculateSteps(totalStepsSinceLastRebootOfDevice: Float) {
        // use Coroutines viewModelScope.launch(Dispatchers.IO)
        // to prevent running database queries on Main Thread
        viewModelScope.launch(Dispatchers.IO) {
            // get current date
            val currentDate = Date()
            // get last Date that's inserted to database
            val lastDate : Date = repository.getUsersLastSavedDate()

            var steps: Steps

            // if steps == null or lastDate isn't same date as current
            if (!lastDateIsSameAsCurrentDate(lastDate, currentDate)) {
                // add new steps row to database with steps.value = 0 and
                // totalStepsSinceLastRebootOfDevice is value from function parameter totalStepsSinceLastRebootOfDevice
                repository.addSteps(Steps(0, sdf.format(currentDate), 0, totalStepsSinceLastRebootOfDevice.toInt(), sharedPreferencesManager.loadUserId(getApplication<Application>().applicationContext)))
            }

            // get current date steps value from database
            steps = repository.getUsersStepsFromSpecificDate(sdf.format(currentDate))

            if (steps.previousSteps == -1) {
                steps.previousSteps = totalStepsSinceLastRebootOfDevice.toInt()
                repository.updateSteps(steps)
            }

            // Steps value from database. Tells how many steps user has already taken today
            val usersStepsFromToday = steps.value

            // variable to count current steps user has taken
            var currentSteps: Int

            if (totalStepsSinceLastRebootOfDevice == 0f) {
                // in case where sensor value is zero
                Log.d(TAG, "Sensorin arvo on nolla")
                // currentSteps = 0
                currentSteps = usersStepsFromToday
            } else {
                if (totalStepsSinceLastRebootOfDevice > usersStepsFromToday) {
                    currentSteps = totalStepsSinceLastRebootOfDevice.toInt() - steps.previousSteps
                } else {
                    currentSteps = steps.previousSteps + totalStepsSinceLastRebootOfDevice.toInt()
                }
            }

            // update current date steps value to database
            Log.d(TAG, "Sama päivä")
            repository.updateSteps(Steps(
                id = steps.id,
                date = steps.date,
                value = currentSteps,
                previousSteps = steps.previousSteps,
                userId = steps.userId
            ))
        }
    }

    /**
     * Function that checks every minute if date has changed.
     * If date has changed then add new steps to database
     */
    fun checkIfDateHasChanged(): Job {
        // prevent running database queries on Main Thread
        return viewModelScope.launch(Dispatchers.IO) {
            while(NonCancellable.isActive) {
                val lastDate : Date = repository.getUsersLastSavedDate()
                if (!lastDateIsSameAsCurrentDate(lastDate, Date())) {
                    // last date not same as current date
                    repository.addSteps(Steps(0, sdf.format(Date()), 0, sharedPreferencesManager.loadTotalStepsSinceLastRebootOfDevice(getApplication<Application>().applicationContext).toInt(), sharedPreferencesManager.loadUserId(getApplication<Application>().applicationContext)))
                }
                delay(60000)
            }
        }
    }
}