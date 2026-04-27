package com.example.smashfeed.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smashfeed.data.local.SmashFeedRoomDatabase
import com.example.smashfeed.data.model.Post
import com.example.smashfeed.data.model.PostWithUser
import com.example.smashfeed.data.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel(context: Context): ViewModel() {
    private val postRepository: PostRepository
    val posts: LiveData<List<Post>>
    val postsWithUser: LiveData<List<PostWithUser>>

    init {
        val postDao = SmashFeedRoomDatabase.getInstance(context).postDAO()
        postRepository = PostRepository(postDao)
        posts = postRepository.getAllPosts()
        postsWithUser = postRepository.getAllPostsWithUser()
    }

    fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.addPost(post)
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.updatePost(post)
        }
    }

    fun toggleLike(post: Post) {
        val updated = if (post.isLiked)
            post.copy(isLiked = false, likes = post.likes - 1)
        else
            post.copy(isLiked = true, likes = post.likes + 1)
        updatePost(updated)
    }

    fun toggleSave(post: Post) {
        val updated = post.copy(saved = !post.saved)
        updatePost(updated)
    }


    fun deletePost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.deletePost(post)
        }
    }
}