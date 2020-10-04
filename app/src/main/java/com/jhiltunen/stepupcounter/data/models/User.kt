package com.jhiltunen.stepupcounter.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val height: Int,
    val weight: Int,
    val gender: String
)