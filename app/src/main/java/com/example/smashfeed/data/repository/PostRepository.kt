package com.example.smashfeed.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.smashfeed.data.local.dao.PostDAO
import com.example.smashfeed.data.local.mapper.toDomain
import com.example.smashfeed.data.local.mapper.toEntity
import com.example.smashfeed.data.model.Post

class PostRepository(
    private val postDAO: PostDAO
){
    fun getAllPosts(): LiveData<List<Post>> {
        return postDAO.getAllPosts().map { listPosts ->
            listPosts.map { it.toDomain() }
        }
    }

    fun addPost(post: Post) {
        postDAO.insert(post.toEntity())
    }

    fun updatePost(post: Post) {
        postDAO.update(post.toEntity())
    }

    fun deletePost(post: Post) {
        postDAO.delete(post.toEntity())
    }
}
