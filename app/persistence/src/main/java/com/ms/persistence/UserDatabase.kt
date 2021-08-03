package com.ms.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    companion object {
        fun getDbInstance(context: Context) : UserDatabase {
            return Room.databaseBuilder(context, UserDatabase::class.java, "user-database").build()
        }
    }

    abstract fun userDao(): UserDao
}