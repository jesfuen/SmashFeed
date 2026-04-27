package com.example.smashfeed.ui.newpost

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.smashfeed.R
import com.example.smashfeed.data.model.Post
import com.example.smashfeed.databinding.ActivityNewPostBinding
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
                binding.ivPreview.setImageURI(it)
                binding.ivPreview.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.ivPreview.background = null
                binding.ivPreview.imageTintList = null
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_post)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val postViewModel = ViewModelProvider(this, PostViewModelFactory(applicationContext))[PostViewModel::class.java]

        binding.ivPreview.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnPost.setOnClickListener {
            val description = binding.etDescription.text.toString().trim()
            val uri = selectedImageUri

            if (uri == null) {
                binding.etDescription.error = "Selecciona una imagen"
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                binding.etDescription.error = "La descripción no puede estar vacía"
                return@setOnClickListener
            }

            val post = Post(
                id = null,
                userId = 1,
                likes = 0,
                description = description,
                path = copyImageToInternal(uri),
                date = LocalDate.now()
            )
            postViewModel.addPost(post)
            finish()
        }
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