package com.example.smashfeed.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smashfeed.data.local.entity.UserSavedEntity

@Dao
interface UserSavedDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(saved: UserSavedEntity)

    @Delete
    suspend fun delete(saved: UserSavedEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM user_saved_table WHERE userId = :userId AND postId = :postId)")
    suspend fun exists(userId: Int, postId: Int): Boolean

    @Query("SELECT postId FROM user_saved_table WHERE userId = :userId")
    fun getSavedPostIds(userId: Int): LiveData<List<Int>>
}