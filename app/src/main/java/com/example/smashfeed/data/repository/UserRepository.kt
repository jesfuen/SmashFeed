package com.example.smashfeed.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.smashfeed.data.local.dao.UserDAO
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

    suspend fun addUser(user: User) {
        userDAO.insert(user.toEntity())
    }

    suspend fun updateUser(user: User) {
        userDAO.update(user.toEntity())
    }

    suspend fun deleteUser(user: User) {
        userDAO.delete(user.toEntity())
    }
}