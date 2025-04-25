package com.example.visionverse.domain.usecase.auth

import com.example.visionverse.domain.model.User
import com.example.visionverse.domain.repository.AuthRepository
import javax.inject.Inject

class CheckUserExistsUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String): User? = repo.getUserByEmail(email)
}