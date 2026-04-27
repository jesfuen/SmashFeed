package com.example.smashfeed.data.local.entity

import androidx.room.Entity

@Entity(tableName = "user_saved_table", primaryKeys = ["userId", "postId"])
data class UserSavedEntity(
    val userId: Int,
    val postId: Int
)