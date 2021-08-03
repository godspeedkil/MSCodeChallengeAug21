package com.ms.app

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.persistence.User
import com.ms.persistence.UserDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Middleware for managing user state and retrieval
 *
 * Intended to act as user state holder if tied to an Activity, in a single-Activity application.
 *
 * Note: User state may need to be refactored into a singleton class in a multiple-Activity application.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    /** Internal, mutable **/
    private val _userLiveData = MutableLiveData<User?>(null)
    /** External, immutable **/
    val userLiveData: LiveData<User?> = _userLiveData

    private val db = UserDatabase.getDbInstance(context)

    private suspend fun isAlreadyInDatabase(user: User) : Boolean {
        return db.userDao().findByEmailAddress(user.emailAddress) != null
    }

    /**
     * Returns true/false for success
     */
    suspend fun addNewUser(user: User) : Boolean {
        return if (isAlreadyInDatabase(user)) {
            false
        } else {
            db.userDao().addNewUser(user)
            _userLiveData.postValue(user)
            true
        }
    }

    fun logoutUser() {
        _userLiveData.postValue(null)
    }

}