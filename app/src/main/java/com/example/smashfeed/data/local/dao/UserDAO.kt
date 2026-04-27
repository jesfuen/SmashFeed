package com.example.smashfeed.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.smashfeed.data.local.entity.UserEntity

@Dao
interface UserDAO {
    @Insert
    suspend fun insert(vararg userEntity: UserEntity)

    @Update
    suspend fun update(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user_table WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?
}