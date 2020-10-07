package com.jhiltunen.stepupcounter.ui.information

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jhiltunen.stepupcounter.data.database.HealthDatabase
import com.jhiltunen.stepupcounter.data.models.BodyMassIndex
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.logic.repository.HealthRepository
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Presents view model for Information fragment.
 * Contains logic to update users information.
 */
@InternalCoroutinesApi
class InformationViewModel(application: Application) : AndroidViewModel(application) {

    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    private val repository: HealthRepository

    // LiveData is used to observe changes to user information
    val getUser: LiveData<User>

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
    }

    /**
     * Function to update users information.
     * @param user User object which contains updated user information
     */
    fun updateUser(user: User) {
        // use Coroutines viewModelScope.launch(Dispatchers.IO)
        // to prevent running database queries on Main Thread
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    /**
     * Function to update bodyMassIndex.
     * @param bodyMassIndex BodyMassIndex object that contains updated data.
     */
    fun addBodyMassIndex(bodyMassIndex: BodyMassIndex) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addBodyMassIndexToDate(bodyMassIndex)
        }
    }
}