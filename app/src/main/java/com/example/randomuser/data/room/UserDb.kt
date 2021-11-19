package com.example.randomuser.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserRoom::class],
    version = 3
)

abstract class UserDb: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "quotes_db"

        @Volatile
        private var instance: UserDb? = null

        fun getInstance(context: Context): UserDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): UserDb {
            return Room.databaseBuilder(context, UserDb::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
        }

    }
}