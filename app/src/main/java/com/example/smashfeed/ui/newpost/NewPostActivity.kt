package com.example.smashfeed.ui.newpost

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.smashfeed.R
import com.example.smashfeed.data.model.Post
import com.example.smashfeed.databinding.ActivityNewPostBinding
import com.example.smashfeed.ui.feed.FeedActivity
import com.example.smashfeed.ui.login.LoginActivity
import com.example.smashfeed.ui.viewmodel.PostViewModel
import com.example.smashfeed.ui.viewmodel.PostViewModelFactory
import java.io.File
import java.time.LocalDate

class NewPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPostBinding
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.contentNewPost.ivPreview.setImageURI(it)
                binding.contentNewPost.ivPreview.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.contentNewPost.ivPreview.background = null
                binding.contentNewPost.ivPreview.imageTintList = null
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_post)

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
                R.id.nav_feed -> {startActivity(Intent(this, FeedActivity::class.java)); true}
                else -> false
            }
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

        val postViewModel = ViewModelProvider(this, PostViewModelFactory(applicationContext))[PostViewModel::class.java]
        val userId = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE)
            .getInt(LoginActivity.KEY_USER_ID, -1)

        binding.contentNewPost.ivPreview.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.contentNewPost.btnPost.setOnClickListener {
            val description = binding.contentNewPost.etDescription.text.toString().trim()
            val uri = selectedImageUri

            if (uri == null) {
                binding.contentNewPost.etDescription.error = "Selecciona una imagen"
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                binding.contentNewPost.etDescription.error = "La descripción no puede estar vacía"
                return@setOnClickListener
            }

            val post = Post(
                id = null,
                userId = userId,
                likes = 0,
                description = description,
                path = copyImageToInternal(uri),
                date = LocalDate.now()
            )
            postViewModel.addPost(post)
            finish()
        }
    }

    private fun logout() {
        getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE).edit { clear() }
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    private fun copyImageToInternal(uri: Uri): String {
        val dir = File(filesDir, "images").also { it.mkdirs() }
        val file = File(dir, "${System.currentTimeMillis()}.jpg")
        contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }
}