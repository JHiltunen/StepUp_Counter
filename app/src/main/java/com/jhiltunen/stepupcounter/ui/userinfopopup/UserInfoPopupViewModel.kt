package com.jhiltunen.stepupcounter.ui.userinfopopup

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jhiltunen.stepupcounter.data.database.HealthDatabase
import com.jhiltunen.stepupcounter.data.models.BodyMassIndex
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.logic.repository.HealthRepository
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

/**
 * Represents view model for UserInfoPopup.
 * Contains logic to add new user to database.
 */
@InternalCoroutinesApi
class UserInfoPopupViewModel (application: Application) : AndroidViewModel(application) {

    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    private val repository: HealthRepository

    /**
     * Initialize view model and required variables
     */
    init {
        // create new Health Data Access Object
        val healthDao = HealthDatabase.getDatabase(application).healthDao()
        // initialize new repository and load userId from shared preferences
        repository = HealthRepository(healthDao, sharedPreferencesManager.loadUserId(getApplication<Application>().applicationContext))
    }

    /**
     * Function to add new user to database. Calls saveUserId function to save new users id to shared preferences
     * @param user User object to be saved to database with data that user has given
     */
    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            // addUser function returns id that's given to new user
            // new users id is saved to sharedPreferences by calling saveUserId
            sharedPreferencesManager.saveUserId(getApplication<Application>().applicationContext, repository.insertIntoUsersAndInitializeSteps(user, SimpleDateFormat("yyyy-MM-dd").format(Date())))
            repository.addBodyMassIndexToDate(BodyMassIndex(0, SimpleDateFormat("yyyy-MM-dd").format(Date()), (user.weight.toFloat() / (user.height.toFloat() / 100.0).pow(2)), sharedPreferencesManager.loadUserId(getApplication<Application>().applicationContext)))
        }
    }
}