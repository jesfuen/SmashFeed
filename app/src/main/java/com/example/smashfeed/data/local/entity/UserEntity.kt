package com.example.smashfeed.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val username: String,
    val password: String,
    val name: String,
    val avatar: String,
    val level: Double,
    val bio: String,
    val followers: Int,
    val followed: Int,
    val totalPosts: Int
)