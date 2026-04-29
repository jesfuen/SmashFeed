package com.example.smashfeed.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smashfeed.data.local.SmashFeedRoomDatabase
import com.example.smashfeed.data.local.dao.UserLikeDAO
import com.example.smashfeed.data.local.dao.UserSavedDAO
import com.example.smashfeed.data.local.entity.UserLikeEntity
import com.example.smashfeed.data.local.entity.UserSavedEntity
import com.example.smashfeed.data.model.Post
import com.example.smashfeed.data.model.PostWithUser
import com.example.smashfeed.data.repository.PostRepository
import com.example.smashfeed.data.repository.UserRepository
import com.example.smashfeed.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel(context: Context) : ViewModel() {

    private val postRepository: PostRepository
    private val userRepository: UserRepository
    private val userLikeDAO: UserLikeDAO
    private val userSavedDAO: UserSavedDAO

    private val userId: Int = context.getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE)
        .getInt(LoginActivity.KEY_USER_ID, -1)

    val postsWithUser = MediatorLiveData<List<PostWithUser>>()

    init {
        val db = SmashFeedRoomDatabase.getInstance(context)
        postRepository = PostRepository(db.postDAO())
        userRepository = UserRepository(db.userDAO())
        userLikeDAO = db.userLikeDAO()
        userSavedDAO = db.userSavedDAO()

        val rawPosts = postRepository.getAllPostsWithUser()
        val likedIds = userLikeDAO.getLikedPostIds(userId)
        val savedIds = userSavedDAO.getSavedPostIds(userId)

        fun combine() {
            val posts = rawPosts.value ?: return
            val liked = likedIds.value ?: emptyList()
            val saved = savedIds.value ?: emptyList()
            postsWithUser.value = posts.map { pwu ->
                pwu.copy(post = pwu.post.copy(
                    isLiked = pwu.post.id in liked,
                    saved = pwu.post.id in saved
                ))
            }
        }

        postsWithUser.addSource(rawPosts) { combine() }
        postsWithUser.addSource(likedIds) { combine() }
        postsWithUser.addSource(savedIds) { combine() }
    }

    fun toggleLike(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            val postId = post.id ?: return@launch
            if (post.isLiked) {
                userLikeDAO.delete(UserLikeEntity(userId, postId))
                postRepository.decrementLikes(postId)
            } else {
                userLikeDAO.insert(UserLikeEntity(userId, postId))
                postRepository.incrementLikes(postId)
            }
        }
    }

    fun toggleSave(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            val postId = post.id ?: return@launch
            if (post.saved) {
                userSavedDAO.delete(UserSavedEntity(userId, postId))
            } else {
                userSavedDAO.insert(UserSavedEntity(userId, postId))
            }
        }
    }

    fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.addPost(post)
            userRepository.incrementTotalPosts(userId)
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.deletePost(post)
        }
    }
}