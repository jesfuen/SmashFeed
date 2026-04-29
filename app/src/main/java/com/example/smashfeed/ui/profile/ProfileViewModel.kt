package com.example.smashfeed.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.smashfeed.data.local.SmashFeedRoomDatabase
import com.example.smashfeed.data.model.PostWithUser
import com.example.smashfeed.data.model.User
import com.example.smashfeed.data.repository.PostRepository
import com.example.smashfeed.data.repository.UserRepository
import com.example.smashfeed.ui.login.LoginActivity

class ProfileViewModel(context: Context) : ViewModel() {

    private val userId: Int = context.getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE)
        .getInt(LoginActivity.KEY_USER_ID, -1)

    val currentUser: LiveData<User?>
    val userPosts: LiveData<List<PostWithUser>>
    val savedPosts: LiveData<List<PostWithUser>>

    init {
        val db = SmashFeedRoomDatabase.getInstance(context)
        val userRepository = UserRepository(db.userDAO())
        val postRepository = PostRepository(db.postDAO())

        currentUser = userRepository.getUserById(userId)
        userPosts = postRepository.getPostsWithUserByUserId(userId)
        savedPosts = postRepository.getSavedPostsWithUser(userId)
    }
}