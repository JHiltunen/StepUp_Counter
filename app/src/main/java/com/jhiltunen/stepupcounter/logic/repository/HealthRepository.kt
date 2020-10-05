package com.jhiltunen.stepupcounter.logic.repository

import androidx.lifecycle.LiveData
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.logic.dao.HealthDao

class HealthRepository (private val healthDao: HealthDao, var id: Long) {

    val getUser: LiveData<User> = healthDao.getUser(id)
    val getUsersSteps: LiveData<List<Steps>> = healthDao.getAllUsersSteps(id)

    suspend fun addUser(user: User): Long {
        return healthDao.addUser(user)
    }

    suspend fun updateUser(user: User) {
        healthDao.updateUser(user)
    }

    fun getUsersStepsCountFromSpecificDate(date: String): LiveData<Int> {
        return healthDao.getUsersStepsCountFromSpecificDate(id, date)
    }

    fun getUsersStepsFromSpecificDate(date: String): Steps {
        return healthDao.getUsersStepsFromSpecificDate(id, date)
    }

    suspend fun addSteps(steps: Steps) {
        healthDao.addSteps(steps)
    }

    suspend fun updateSteps(steps: Steps) {
        healthDao.updateSteps(steps)
    }
}