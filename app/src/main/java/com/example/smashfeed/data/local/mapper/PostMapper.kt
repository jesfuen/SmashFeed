package com.example.smashfeed.data.local.mapper

import com.example.smashfeed.data.local.entity.PostEntity
import com.example.smashfeed.data.model.Post
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private val DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE

fun PostEntity.toDomain(): Post {
    val parsedDate = try {
        LocalDate.parse(this.date, DATE_FORMATTER)
    } catch (e: DateTimeParseException) {
        LocalDate.now()
    }
    return Post(
        id = this.id,
        userId = this.userId,
        likes = this.likes,
        saved = false,
        isLiked = false,
        description = this.description,
        path = this.path,
        date = parsedDate
    )
}

fun Post.toEntity(): PostEntity {
    return PostEntity(
        id = this.id,
        userId = this.userId,
        likes = this.likes,
        description = this.description,
        path = this.path,
        date = this.date.toString()
    )
}