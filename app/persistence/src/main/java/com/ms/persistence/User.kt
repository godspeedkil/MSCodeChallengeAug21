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
) {

    companion object {

        /**
         * Return a [User] instance, if possible
         */
        fun attemptInstantiateUser(
            emailAddress: CharSequence?,
            firstName: CharSequence?,
            password: CharSequence?,
            website: CharSequence?
        ) : User? {
            if (emailAddress.isNullOrBlank() || password.isNullOrBlank()) {
                return null
            }

            return User(
                emailAddress = emailAddress.toString(),
                firstName =
                    if (firstName.isNullOrBlank()) {
                        null
                    } else {
                        firstName.toString()
                    },
                password = password.toString(),
                website =
                    if (website.isNullOrBlank()) {
                        null
                    } else {
                        website.toString()
                    }
            )
        }

    }

}