package com.example.smashfeed.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PostWithUserEntity(
    @Embedded val postEntity: PostEntity,
    @Relation(parentColumn = "userId", entityColumn = "id")
    val userEntity: UserEntity
)
