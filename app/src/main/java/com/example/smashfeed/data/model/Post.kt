package com.example.smashfeed.data.model

data class Post(
    val id: Int?,
    val userId: Int,
    val likes: Int,
    val saved: Int,
    val description: String,
    val path: String
)
