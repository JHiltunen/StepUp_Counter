package com.jhiltunen.stepupcounter.logic.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.logic.dao.HealthDao
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager

class HealthRepository (private val healthDao: HealthDao, id : Long) {

    val getUser: LiveData<User> = healthDao.getUser(id)

    suspend fun addUser(user: User): Long {
        return healthDao.addUser(user)
    }

    suspend fun updateUser(user: User) {
        healthDao.updateUser(user)
    }

    /*suspend fun getUser(userId: Long): LiveData<User> {
        return healthDao.getUser(userId)
    }*/

    suspend fun addSteps(steps: Steps) {
        healthDao.addSteps(steps)
    }
}