package com.ms.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE emailAddress LIKE :emailAddress")
    suspend fun findByEmailAddress(emailAddress: String): User?

    @Query("SELECT * FROM user WHERE emailAddress LIKE :emailAddress AND password LIKE :password")
    suspend fun findByEmailAndPassword(emailAddress: String, password: String): User?

    @Insert
    suspend fun addNewUser(user: User)

    @Query("DELETE FROM user WHERE emailAddress LIKE :emailAddress")
    suspend fun deleteUser(emailAddress: String)
}