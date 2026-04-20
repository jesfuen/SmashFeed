package com.example.smashfeed.data.model

data class User (
    val id: Int?,
    val name: String,
    val avatar: String,
    val level: Double,
    val bio: String,
    val followers: Int,
    val followed: Int,
    val totalPosts: Int
)
