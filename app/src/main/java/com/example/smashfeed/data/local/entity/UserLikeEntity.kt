package com.example.smashfeed.data.local.entity

import androidx.room.Entity

@Entity(tableName = "user_like_table", primaryKeys = ["userId", "postId"])
data class UserLikeEntity(
    val userId: Int,
    val postId: Int
)