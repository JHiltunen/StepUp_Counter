package com.jhiltunen.stepupcounter.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "steps", foreignKeys = [ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userId"), onDelete = ForeignKey.CASCADE)])
data class Steps (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    val value: Int,
    val userId: Int

)