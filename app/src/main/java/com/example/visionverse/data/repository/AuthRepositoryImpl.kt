package com.example.visionverse.data.repository

import com.example.visionverse.data.local.user.UserDao
import com.example.visionverse.data.local.user.UserEntity
import com.example.visionverse.domain.model.User
import com.example.visionverse.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : AuthRepository {
    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)?.let {
            User(it.email, it.password)
        }
    }

    override suspend fun createUser(user: User) {
        userDao.insertUser(UserEntity(user.email, user.password))
    }
}
