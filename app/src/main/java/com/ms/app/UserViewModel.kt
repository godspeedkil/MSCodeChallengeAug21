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

@HiltViewModel
class UserViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    /** Internal, mutable **/
    private val _userLiveData = MutableLiveData<User?>(null)
    /** External, immutable **/
    val userLiveData: LiveData<User?> = _userLiveData

    private val db = UserDatabase.getDbInstance(context)

    suspend fun isAlreadyInDatabase(user: User) : Boolean {
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
            _userLiveData.value = user
            true
        }
    }

}