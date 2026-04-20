package com.example.smashfeed.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.smashfeed.data.local.entity.PostEntity

@Dao
interface PostDAO {
    @Insert
    fun insert(vararg postEntity: PostEntity)

    @Update
    fun update(postEntity: PostEntity)

    @Delete
    fun delete(postEntity: PostEntity)
}