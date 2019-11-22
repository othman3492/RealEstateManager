package com.openclassrooms.realestatemanager.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.User


private const val DATABASE = "properties"

@Database(
        entities = [RealEstate::class, User::class],
        version = 1,
        exportSchema = false
)


internal abstract class RoomDatabase : RoomDatabase() {

    // Return Dao
    abstract fun roomPropertyDao(): PropertyDao

    abstract fun roomUserDao(): UserDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: RoomDatabase? = null

        // Ensure that no more than one database is created
        fun getInstance(context: Context): RoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): RoomDatabase {
            return Room.databaseBuilder(context, RoomDatabase::class.java, DATABASE)
                    .build()
        }
    }

}