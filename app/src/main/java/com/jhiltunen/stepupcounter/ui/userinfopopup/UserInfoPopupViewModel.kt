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

@InternalCoroutinesApi
class UserInfoPopupViewModel (application: Application) : AndroidViewModel(application) {

    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    private val repository: HealthRepository

    init {
        val healthDao = HealthDatabase.getDatabase(application).healthDao()
        repository = HealthRepository(healthDao, sharedPreferencesManager.loadUserId(getApplication<Application>().applicationContext))
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            saveUserId(repository.addUser(user))
        }
    }

    private fun saveUserId(userId: Long) {
        Log.d("TAG", "saveId: $userId")
        val sp = getApplication<Application>().getSharedPreferences("USER", AppCompatActivity.MODE_PRIVATE)
        sp.edit().apply{
            putLong("userId", userId)
            apply()
        }
    }
}