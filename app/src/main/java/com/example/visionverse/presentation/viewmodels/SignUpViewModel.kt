package com.example.visionverse.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visionverse.data.local.DataStoreManager
import com.example.visionverse.domain.model.User
import com.example.visionverse.domain.usecase.auth.CreateUserUseCase
import com.example.visionverse.presentation.screen.auth.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.visionverse.domain.usecase.auth.CheckUserExistsUseCase

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val checkUserExists: CheckUserExistsUseCase,
    private val createUser: CreateUserUseCase,
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

    fun signUp(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            // Check if user already exists
            val existingUser = checkUserExists(uiState.email)
            if (existingUser != null) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "User already exists. Please sign in."
                )
                return@launch
            }

            // Create the new user
            createUser(User(uiState.email, uiState.password))
            dataStoreManager.setLoggedIn(uiState.email)
            onSuccess()
        }
    }
}

