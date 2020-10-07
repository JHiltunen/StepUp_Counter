package com.jhiltunen.stepupcounter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jhiltunen.stepupcounter.data.models.BodyMassIndex
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.logic.dao.HealthDao
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [User::class, Steps::class, BodyMassIndex::class], version = 1, exportSchema = false)
abstract class HealthDatabase: RoomDatabase() {

    abstract fun healthDao() : HealthDao

    companion object {
        // As soon as we access database, it will be made visible to all the other threads
        @Volatile
        private var INSTANCE: HealthDatabase? = null

        @InternalCoroutinesApi
        fun getDatabase(context: Context): HealthDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            // This block of code will be run only at one thread at a time.
            // So we can't have multiple instances of it being created at the same time.
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java,
                    "health_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}