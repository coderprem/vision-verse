package com.example.visionverse.domain.usecase.auth

import com.example.visionverse.domain.model.User
import com.example.visionverse.domain.repository.AuthRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke(user: User) = repo.createUser(user)
}