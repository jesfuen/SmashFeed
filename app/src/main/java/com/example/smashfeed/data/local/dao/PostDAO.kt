package com.example.smashfeed.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.smashfeed.data.local.entity.PostEntity
import com.example.smashfeed.data.local.entity.PostWithUserEntity

@Dao
interface PostDAO {
    @Insert
    suspend fun insert(vararg postEntity: PostEntity)

    @Update
    suspend fun update(postEntity: PostEntity)

    @Delete
    suspend fun delete(postEntity: PostEntity)

    // Querys de Post
    @Query("SELECT * FROM post_table")
    fun getAllPosts(): LiveData<List<PostEntity>>

    @Transaction
    @Query("SELECT * FROM post_table")
    fun getAllPostsWithUser(): LiveData<List<PostWithUserEntity>>

    @Query("UPDATE post_table SET likes = likes + 1 WHERE id = :postId")
    suspend fun incrementLikes(postId: Int)

    @Query("UPDATE post_table SET likes = likes - 1 WHERE id = :postId")
    suspend fun decrementLikes(postId: Int)

    @Transaction
    @Query("SELECT * FROM post_table WHERE userId = :userId")
    fun getPostsWithUserByUserId(userId: Int): LiveData<List<PostWithUserEntity>>

    @Transaction
    @Query("SELECT p.* FROM post_table p INNER JOIN user_saved_table s ON p.id = s.postId WHERE s.userId = :userId")
    fun getSavedPostsWithUser(userId: Int): LiveData<List<PostWithUserEntity>>
}
