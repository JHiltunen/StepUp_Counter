package com.jhiltunen.stepupcounter.ui.userinfopopup

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jhiltunen.stepupcounter.data.database.HealthDatabase
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.logic.repository.HealthRepository
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

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
            saveUserId(repository.addUser(user))
        }
    }

    /**
     * Saves userId given in parameters to sharedPreferences.
     * @param userId That's going to be saved to sharedPreferences.
     */
    private fun saveUserId(userId: Long) {
        Log.d("TAG", "saveId: $userId")
        // get shared preference
        val sp = getApplication<Application>().getSharedPreferences("USER", AppCompatActivity.MODE_PRIVATE)
        // edit shared preferences
        sp.edit().apply{
            // put userId as long
            putLong("userId", userId)
            apply()
            // apply changes and save
        }
    }
}