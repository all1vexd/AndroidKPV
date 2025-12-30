package ru.itis.hw5.utils

import at.favre.lib.crypto.bcrypt.BCrypt
import java.security.MessageDigest
import java.util.UUID

object PasswordHasher {

    fun generateSalt(): String {
        return UUID.randomUUID().toString()
    }

    fun hashPassword(
        password: String,
        salt: String
    ): String {
        val saltedPassword = password + salt

        val bytes = saltedPassword.toByteArray()
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(bytes)

        return hash.joinToString("") { byte ->
            String.format("%02x", byte)
        }
    }

    fun verifyPassword(
        password: String,
        hashedPassword: String,
        salt: String
    ): Boolean {
        val newHash = hashPassword(password, salt)
        return  newHash == hashedPassword
    }
}