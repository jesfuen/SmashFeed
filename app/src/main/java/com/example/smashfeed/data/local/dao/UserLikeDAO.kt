package com.example.smashfeed.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smashfeed.data.local.entity.UserLikeEntity

@Dao
interface UserLikeDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(like: UserLikeEntity)

    @Delete
    suspend fun delete(like: UserLikeEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM user_like_table WHERE userId = :userId AND postId = :postId)")
    suspend fun exists(userId: Int, postId: Int): Boolean

    @Query("SELECT postId FROM user_like_table WHERE userId = :userId")
    fun getLikedPostIds(userId: Int): LiveData<List<Int>>
}