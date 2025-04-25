package com.example.visionverse.presentation.screen.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visionverse.data.local.DataStoreManager
import com.example.visionverse.domain.usecase.auth.CheckUserExistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val checkUserExists: CheckUserExistsUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    var uiState by mutableStateOf(AuthUiState())
        private set

    fun onEmailChange(email: String) {
        uiState = uiState.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password)
    }

    fun signIn(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            val user = checkUserExists(uiState.email)  // Check if user exists
            if (user == null) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "User not found. Please sign up."
                )
                return@launch
            }

            // Check if password matches
            if (user.password != uiState.password) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Incorrect password"
                )
                return@launch
            }

            // If user exists and password matches, login the user
            dataStoreManager.setLoggedIn(uiState.email)
            onSuccess()
        }
    }
}
