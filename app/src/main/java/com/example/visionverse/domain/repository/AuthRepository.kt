package com.example.visionverse.domain.repository

import com.example.visionverse.domain.model.User

interface AuthRepository {
    suspend fun getUserByEmail(email: String): User?
    suspend fun createUser(user: User)
}
