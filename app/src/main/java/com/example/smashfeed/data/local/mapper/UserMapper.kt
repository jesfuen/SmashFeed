package com.example.smashfeed.data.local.mapper

import com.example.smashfeed.data.local.entity.UserEntity
import com.example.smashfeed.data.model.User

fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        avatar = this.avatar,
        level = this.level,
        bio = this.bio,
        followers = this.followers,
        followed = this.followed,
        totalPosts = this.totalPosts
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        avatar = this.avatar,
        level = this.level,
        bio = this.bio,
        followers = this.followers,
        followed = this.followed,
        totalPosts = this.totalPosts
    )
}