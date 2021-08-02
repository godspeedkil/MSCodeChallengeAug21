package com.ms.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val emailAddress: String,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "website") val website: String?
)