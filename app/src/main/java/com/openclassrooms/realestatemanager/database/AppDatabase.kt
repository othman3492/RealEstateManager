package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.Type
import com.openclassrooms.realestatemanager.models.User


@Database(
        entities = [RealEstate::class, User::class, Type::class],
        version = 1,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Return Dao
    abstract fun realEstateDao(): RealEstateDao

    abstract fun userDao(): UserDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        // Ensure that no more than one database is created
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(context.applicationContext,
                                AppDatabase::class.java, "RealEstateDatabase.db")
                                .build()
                    }
                }
            }
            return instance
        }
    }

}