package com.example.smashfeed.ui.feed

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smashfeed.R
import com.example.smashfeed.databinding.ActivityFeedBinding
import com.example.smashfeed.ui.feed.adapter.FeedAdapter
import com.example.smashfeed.ui.login.LoginActivity
import com.example.smashfeed.ui.newpost.NewPostActivity
import com.example.smashfeed.ui.profile.ProfileActivity
import com.example.smashfeed.ui.viewmodel.PostViewModel
import com.example.smashfeed.ui.viewmodel.PostViewModelFactory

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feed)

        ViewCompat.setOnApplyWindowInsetsListener(binding.contentFeed.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.contentFeed.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navigationView.setNavigationItemSelectedListener { item ->
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            when (item.itemId) {
                R.id.nav_logout -> { logout(); true }
                R.id.nav_feed -> { true }
                R.id.nav_profile -> { startActivity((Intent(this, ProfileActivity::class.java))); true}
                else -> false
            }
        }

        val postViewModel = ViewModelProvider(this, PostViewModelFactory(applicationContext))[PostViewModel::class.java]

        val adapter = FeedAdapter(
            posts = emptyList(),
            onLikeClick = { post -> postViewModel.toggleLike(post) },
            onSaveClick = { post -> postViewModel.toggleSave(post) }
        )

        binding.contentFeed.recyclerViewFeed.layoutManager = LinearLayoutManager(this)
        binding.contentFeed.recyclerViewFeed.adapter = adapter

        postViewModel.postsWithUser.observe(this) { posts ->
            adapter.updatePosts(posts)
        }

        binding.contentFeed.fabNewPost.setOnClickListener {
            startActivity(Intent(this, NewPostActivity::class.java))
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun logout() {
        getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE).edit { clear() }
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}