package com.jhiltunen.stepupcounter.logic.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User

@Dao
interface HealthDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSteps(steps: Steps)

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: Long): LiveData<User>

    @Query ("SELECT * FROM steps")
    fun getAllUsersSteps(): LiveData<List<Steps>>
}