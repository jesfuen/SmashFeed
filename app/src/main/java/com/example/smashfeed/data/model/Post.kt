package com.example.smashfeed.data.model

import java.time.LocalDate

data class Post(
    val id: Int?,
    val userId: Int,
    val likes: Int,
    val saved: Int,
    val description: String,
    val path: String,
    val date: LocalDate
)
