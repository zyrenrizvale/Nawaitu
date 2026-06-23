package com.nawaitu.app.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nawaitu.app.data.local.NawaitDatabase
import com.nawaitu.app.data.local.SessionManager
import com.nawaitu.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val userName: String = "",
    val userId: Long = -1L
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val database = NawaitDatabase.getDatabase(application)
    private val sessionManager = SessionManager(application)
    private val authRepository = AuthRepository(database, sessionManager)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId.first()
            if (userId > 0) {
                val user = authRepository.getUserById(userId)
                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    userName = user?.name ?: "",
                    userId = userId
                )
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Email dan password tidak boleh kosong")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                val user = result.getOrNull()!!
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    userName = user.name,
                    userId = user.id
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Login gagal"
                )
            }
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        when {
            name.isBlank() || email.isBlank() || password.isBlank() -> {
                _uiState.value = _uiState.value.copy(error = "Semua field harus diisi")
                return
            }
            password != confirmPassword -> {
                _uiState.value = _uiState.value.copy(error = "Password tidak cocok")
                return
            }
            password.length < 6 -> {
                _uiState.value = _uiState.value.copy(error = "Password minimal 6 karakter")
                return
            }
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authRepository.register(name, email, password)
            if (result.isSuccess) {
                val user = result.getOrNull()!!
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    userName = user.name,
                    userId = user.id
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Registrasi gagal"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
