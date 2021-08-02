package com.ms.persistence

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class UserDatabaseTest {

    private lateinit var db: UserDatabase
    private lateinit var userDao: UserDao

    private val user1 = User(
        emailAddress = "phony.address.1@gmail.com",
        firstName = "John Doe 1",
        password = "pass1",
        website = "website1"
    )

    private val user2 = User(
        emailAddress = "phony.address.2@gmail.com",
        firstName = null,
        password = "pass2",
        website = null
    )

    @Before
    fun setup() {
        db = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java, "database-name"
        ).build()

        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun close() {
        db.close()
    }

    @Test
    fun canAddNewUser() = runBlocking {
        userDao.addNewUser(user1)

        userDao.findByEmailAddress(user1.emailAddress) shouldNotBe null
    }

    @Test
    fun canAddUserWithNullFields() = runBlocking {
        userDao.addNewUser(user2)

        userDao.findByEmailAddress(user2.emailAddress) shouldNotBe null
    }

    @Test
    fun cannotAddUserTwice() = runBlocking {
        userDao.addNewUser(user2)

        val exception = shouldThrowExactly<SQLiteConstraintException> { userDao.addNewUser(user2) }
        exception shouldNotBe null
    }

    @Test
    fun nonExistentUserReturnsNull() = runBlocking {
        userDao.findByEmailAddress("not.a.valid.email") shouldBe null
    }

    @Test
    fun canRetrieveExistingUser() = runBlocking {
        userDao.addNewUser(user2)

        val retrievedUser = userDao.findByEmailAddress(user2.emailAddress)

        retrievedUser?.emailAddress shouldBe user2.emailAddress
        retrievedUser?.firstName shouldBe user2.firstName
        retrievedUser?.password shouldBe user2.password
        retrievedUser?.website shouldBe user2.website
    }

    @Test
    fun canDeleteUser() = runBlocking {
        userDao.addNewUser(user1)
        userDao.deleteUser(user1.emailAddress)

        userDao.findByEmailAddress(user1.emailAddress) shouldBe null
    }

}