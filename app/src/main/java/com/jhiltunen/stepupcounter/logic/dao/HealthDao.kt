package com.jhiltunen.stepupcounter.logic.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User

@Dao
interface HealthDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSteps(steps: Steps)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateSteps(steps: Steps)

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: Long): LiveData<User>

    @Query("SELECT value FROM steps WHERE userId = :userId AND date = :date")
    fun getUsersStepsCountFromSpecificDate(userId: Long, date: String): LiveData<Int>

    @Query("SELECT * FROM steps WHERE userId = :userId AND date = :date")
    fun getUsersStepsFromSpecificDate(userId: Long, date: String): Steps

    @Query ("SELECT * FROM steps WHERE userId = :userId")
    fun getAllUsersSteps(userId: Long): LiveData<List<Steps>>
}