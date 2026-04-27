package com.example.smashfeed.ui.feed

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import com.example.smashfeed.R
import com.example.smashfeed.databinding.ActivityFeedBinding
import com.example.smashfeed.ui.feed.adapter.FeedAdapter
import com.example.smashfeed.ui.newpost.NewPostActivity
import com.example.smashfeed.ui.viewmodel.PostViewModel
import com.example.smashfeed.ui.viewmodel.PostViewModelFactory

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feed)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val postViewModel = ViewModelProvider(this, PostViewModelFactory(applicationContext))[PostViewModel::class.java]

        val adapter = FeedAdapter(
            posts = emptyList(),
            onLikeClick = { post -> postViewModel.toggleLike(post) },
            onSaveClick = { post -> postViewModel.toggleSave(post) }
        )

        binding.recyclerViewFeed.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFeed.adapter = adapter

        postViewModel.postsWithUser.observe(this) { posts ->
            adapter.updatePosts(posts)
        }

        binding.fabNewPost.setOnClickListener {
            startActivity(Intent(this, NewPostActivity::class.java))

        }
    }
}