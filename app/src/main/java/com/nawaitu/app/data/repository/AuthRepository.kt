package com.nawaitu.app.data.repository

import com.nawaitu.app.data.local.NawaitDatabase
import com.nawaitu.app.data.local.SessionManager
import com.nawaitu.app.data.model.User

class AuthRepository(
    private val database: NawaitDatabase,
    private val sessionManager: SessionManager
) {
    val currentUserId = sessionManager.currentUserId
    val currentUserName = sessionManager.currentUserName

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val user = database.userDao().login(email.trim(), password)
            if (user != null) {
                sessionManager.saveSession(user.id, user.name, user.email)
                Result.success(user)
            } else {
                Result.failure(Exception("Email atau password salah"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<User> {
        return try {
            val existing = database.userDao().findByEmail(email.trim())
            if (existing != null) {
                return Result.failure(Exception("Email sudah terdaftar"))
            }
            val user = User(name = name.trim(), email = email.trim(), password = password)
            val id = database.userDao().insert(user)
            sessionManager.saveSession(id, name.trim(), email.trim())
            Result.success(user.copy(id = id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }

    suspend fun getUserById(id: Long): User? {
        return database.userDao().findById(id)
    }
}
