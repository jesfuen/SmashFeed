package com.example.smashfeed.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.smashfeed.data.local.dao.PostDAO
import com.example.smashfeed.data.local.entity.PostWithUserEntity
import com.example.smashfeed.data.local.mapper.toDomain
import com.example.smashfeed.data.local.mapper.toEntity
import com.example.smashfeed.data.model.Post
import com.example.smashfeed.data.model.PostWithUser

class PostRepository(
    private val postDAO: PostDAO
){
    fun getAllPosts(): LiveData<List<Post>> {
        return postDAO.getAllPosts().map { listPosts ->
            listPosts.map { it.toDomain() }
        }
    }

    fun getAllPostsWithUser(): LiveData<List<PostWithUser>> {
        return postDAO.getAllPostsWithUser().map { listPostsWithUser ->
            listPostsWithUser.mapNotNull { it.toDomain() }
        }
    }

    suspend fun addPost(post: Post) {
        postDAO.insert(post.toEntity())
    }

    suspend fun updatePost(post: Post) {
        postDAO.update(post.toEntity())
    }

    suspend fun deletePost(post: Post) {
        postDAO.delete(post.toEntity())
    }

    suspend fun incrementLikes(postId: Int) = postDAO.incrementLikes(postId)
    suspend fun decrementLikes(postId: Int) = postDAO.decrementLikes(postId)

    fun getPostsWithUserByUserId(userId: Int): LiveData<List<PostWithUser>> {
        return postDAO.getPostsWithUserByUserId(userId).map { list ->
            list.mapNotNull { it.toDomain() }
        }
    }

    fun getSavedPostsWithUser(userId: Int): LiveData<List<PostWithUser>> {
        return postDAO.getSavedPostsWithUser(userId).map { list ->
            list.mapNotNull { it.toDomain() }
        }
    }
}
