package com.example.smashfeed.data.local.mapper

import com.example.smashfeed.data.local.entity.PostWithUserEntity
import com.example.smashfeed.data.model.PostWithUser

fun PostWithUserEntity.toDomain(): PostWithUser {
    return PostWithUser(
        post = postEntity.toDomain(),
        user = userEntity.toDomain()
    )
}

