package ru.itis.hw5.data.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import ru.itis.hw5.constants.StringResources
import ru.itis.hw5.data.database.AppDatabase
import ru.itis.hw5.data.database.entities.User
import ru.itis.hw5.utils.PasswordHasher
import java.lang.Exception

class UserRepository (
    private val database: AppDatabase,
    context: Context
){
    private val userDao = database.userDao()
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveSession(userId: Int) {
        prefs.edit().putInt("current_user_id", userId).apply()
    }

    fun getSession(): Int {
        return prefs.getInt("current_user_id", -1)
    }

    fun clearSession() {
        prefs.edit().remove("current_user_id").apply()
    }

    suspend fun register(name: String, email: String, password: String, passwordRepeat: String): RegistrationResult {
        return try {

            if (!password.equals(passwordRepeat)) {
                return RegistrationResult.Error(StringResources.PASSWORDS_DONT_MATCH)
            }

            val exists = userDao.checkEmailExists(email) > 0
            if (exists) {
                return RegistrationResult.Error(StringResources.EXISTING_USER_ERROR)
            }

            val salt = PasswordHasher.generateSalt()
            val hashedPassword = PasswordHasher.hashPassword(password, salt)

            val user = User().apply {
                userName = name
                this.email = email
                this.salt = salt
                this.passwordHash = hashedPassword
                registrationDate = System.currentTimeMillis()
            }

            userDao.insert(user)
            RegistrationResult.Success(user)

        } catch (e: Exception) {
            e.printStackTrace()
            RegistrationResult.Error(StringResources.REGISTRATION_ERROR + "${e.message}")
        }
    }

    suspend fun loginUser(email: String, password: String): LoginResult {
        return try {

            val userExists = userDao.checkEmailExists(email) > 0

            if (!userExists) {
                return LoginResult.UserNotFound
            }

            val user = userDao.getUserByEmail(email) ?: return LoginResult.InvalidCredentials

            val isPasswordCorrect = PasswordHasher.verifyPassword(
                password = password,
                hashedPassword = user.passwordHash,
                salt = user.salt
            )

            if (!isPasswordCorrect) {
                return LoginResult.InvalidCredentials
            }

            if (user.isDeleted) {
                val sevenDaysAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000

                return if (user.deletedDate != null && user.deletedDate!! > sevenDaysAgo) {
                    LoginResult.AccountDeleted(canRestore = true, user = user)
                } else {
                    LoginResult.AccountDeleted(canRestore = false, user = null)
                }

            }

            LoginResult.Success(user)

        } catch (e: Exception) {
            e.printStackTrace()
            LoginResult.Error(e.message?: StringResources.ERROR)
        }
    }

    suspend fun deleteAccount(userId: Int): Boolean {
        return try {
            userDao.markAsDeleted(userId)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun restoreAccount(email: String): Boolean {
        return try {
            userDao.restoreUser(email)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun permanentlyDeleteAccount(email: String): Boolean {
        return try {
            userDao.permanentlyDeleteUser(email)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    suspend fun updateUser(user: User): Boolean {
        return try {
            userDao.update(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
}

sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    data class AccountDeleted(val canRestore: Boolean, val user: User?) : LoginResult()
    object InvalidCredentials : LoginResult()
    object UserNotFound: LoginResult()
    data class Error(val message: String) : LoginResult()
}

sealed class RegistrationResult {
    data class Success(val user: User): RegistrationResult()
    data class Error(val message: String): RegistrationResult()
}