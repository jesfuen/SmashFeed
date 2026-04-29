package com.example.smashfeed.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smashfeed.R
import com.example.smashfeed.databinding.ActivityProfileBinding
import com.example.smashfeed.ui.feed.FeedActivity
import com.example.smashfeed.ui.login.LoginActivity
import com.example.smashfeed.ui.profile.adapter.ProfileAdapter
import com.google.android.material.tabs.TabLayout
import java.io.File
import androidx.core.content.edit

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(binding.contentNewPost.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.contentNewPost.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navigationView.setNavigationItemSelectedListener { item ->
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            when (item.itemId) {
                R.id.nav_logout -> { logout(); true }
                R.id.nav_feed -> { startActivity(Intent(this, FeedActivity::class.java)); true }
                R.id.nav_profile -> { true }
                else -> false
            }
        }

        val viewModel = ViewModelProvider(this, ProfileViewModelFactory(applicationContext))[ProfileViewModel::class.java]

        val adapter = ProfileAdapter()
        binding.contentNewPost.rvPostsGrid.layoutManager = GridLayoutManager(this, 3)
        binding.contentNewPost.rvPostsGrid.adapter = adapter

        viewModel.currentUser.observe(this) { user ->
            user ?: return@observe
            binding.contentNewPost.tvUsername.text = user.username
            binding.contentNewPost.tvBio.text = user.bio
            binding.contentNewPost.tvPosts.text = getString(R.string.posts_count, user.totalPosts)
            val avatarFile = File(user.avatar)
            if (avatarFile.exists()) {
                binding.contentNewPost.ivAvatar.setImageURI(Uri.fromFile(avatarFile))
            }
        }

        viewModel.userPosts.observe(this) { posts ->
            if (binding.contentNewPost.tabLayout.selectedTabPosition == 0) {
                adapter.submitList(posts)
            }
        }

        viewModel.savedPosts.observe(this) { posts ->
            binding.contentNewPost.tvSaved.text = getString(R.string.saved_count, posts.size)
            if (binding.contentNewPost.tabLayout.selectedTabPosition == 1) {
                adapter.submitList(posts)
            }
        }

        binding.contentNewPost.btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        binding.contentNewPost.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> viewModel.userPosts.value?.let { adapter.submitList(it) }
                    1 -> viewModel.savedPosts.value?.let { adapter.submitList(it) }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

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