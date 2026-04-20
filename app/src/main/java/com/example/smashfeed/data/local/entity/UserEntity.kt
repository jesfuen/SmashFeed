package com.example.smashfeed.data.local.entity

import android.media.Image
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val avatar: String, // Con la ruta a la imagen
    val level: Double,
    val bio: String,
    val followers: Int,
    val followed: Int,
    val totalPosts: Int
)