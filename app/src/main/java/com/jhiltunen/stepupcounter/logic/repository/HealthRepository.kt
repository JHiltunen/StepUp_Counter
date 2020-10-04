package com.jhiltunen.stepupcounter.logic.repository

import androidx.lifecycle.LiveData
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.logic.dao.HealthDao

class HealthRepository (private val healthDao: HealthDao) {
    val getAllUsersSteps: LiveData<List<Steps>> = healthDao.getAllUsersSteps()

    suspend fun addUser(user: User) {
        healthDao.addUser(user)
    }

    suspend fun addSteps(steps: Steps) {
        healthDao.addSteps(steps)
    }
}