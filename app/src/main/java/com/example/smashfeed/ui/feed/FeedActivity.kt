package com.example.smashfeed.ui.feed

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smashfeed.R
import com.example.smashfeed.ui.feed.adapter.FeedAdapter
import com.example.smashfeed.ui.login.LoginActivity
import com.example.smashfeed.ui.newpost.NewPostActivity
import com.example.smashfeed.ui.viewmodel.PostViewModel
import com.example.smashfeed.ui.viewmodel.PostViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class FeedActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feed)

        drawerLayout = findViewById(R.id.drawerLayout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFeed)
        val fab = findViewById<FloatingActionButton>(R.id.fabNewPost)
        val contentRoot = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)

        ViewCompat.setOnApplyWindowInsetsListener(contentRoot) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawer(GravityCompat.START)
            when (item.itemId) {
                R.id.nav_logout -> { logout(); true }
                else -> false
            }
        }

        val postViewModel = ViewModelProvider(this, PostViewModelFactory(applicationContext))[PostViewModel::class.java]

        val adapter = FeedAdapter(
            posts = emptyList(),
            onLikeClick = { post -> postViewModel.toggleLike(post) },
            onSaveClick = { post -> postViewModel.toggleSave(post) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        postViewModel.postsWithUser.observe(this) { posts ->
            adapter.updatePosts(posts)
        }

        fab.setOnClickListener {
            startActivity(Intent(this, NewPostActivity::class.java))
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
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