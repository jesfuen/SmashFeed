package com.example.smashfeed.data.model

import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate

data class Post(
    val id: Int?,
    val userId: Int,
    val likes: Int,
    val saved: Boolean = false,
    val isLiked: Boolean = false,
    val description: String,
    val path: String,
    val date: LocalDate,
    val latitude: Double?,
    val longitude: Double?

)
