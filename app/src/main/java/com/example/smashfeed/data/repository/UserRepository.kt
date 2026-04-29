package com.example.smashfeed.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.smashfeed.data.local.dao.UserDAO
import com.example.smashfeed.data.local.entity.UserEntity
import com.example.smashfeed.data.local.mapper.toDomain
import com.example.smashfeed.data.local.mapper.toEntity
import com.example.smashfeed.data.model.User

class UserRepository(
    private val userDAO: UserDAO
) {
    fun getAllUsers(): LiveData<List<User>> {
        return userDAO.getAllUsers().map { listUsers ->
            listUsers.map { it.toDomain() }
        }
    }

    fun getUserById(userId: Int): LiveData<User?> {
        return userDAO.getUserById(userId).map { it?.toDomain() }
    }

    suspend fun incrementTotalPosts(userId: Int) {
        userDAO.incrementTotalPosts(userId)
    }

    suspend fun addUser(user: User) {
        userDAO.insert(user.toEntity())
    }

    suspend fun updateUser(user: User) {
        userDAO.update(user.toEntity())
    }

    suspend fun deleteUser(user: User) {
        userDAO.delete(user.toEntity())
    }

    suspend fun loginOrRegister(username: String, password: String): User? {
        val existing = userDAO.getUserByUsername(username)
        return when {
            existing == null -> {
                userDAO.insert(
                    UserEntity(
                        username = username,
                        password = password,
                        name = username,
                        avatar = "",
                        level = 0.0,
                        bio = "",
                        followers = 0,
                        followed = 0,
                        totalPosts = 0
                    )
                )
                userDAO.getUserByUsername(username)?.toDomain()
            }
            existing.password == password -> existing.toDomain()
            else -> null
        }
    }
}