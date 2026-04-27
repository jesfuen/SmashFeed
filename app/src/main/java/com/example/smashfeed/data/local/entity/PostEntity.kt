package com.example.smashfeed.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_table")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val userId: Int,
    val likes: Int,
    val description: String,
    val path: String,
    val date: String
)
