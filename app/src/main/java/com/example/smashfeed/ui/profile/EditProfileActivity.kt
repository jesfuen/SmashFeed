package com.example.smashfeed.ui.profile

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
import com.example.smashfeed.databinding.ActivityEditProfileBinding
import com.example.smashfeed.ui.viewmodel.UserViewModel
import com.example.smashfeed.ui.viewmodel.UserViewModelFactory
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var selectedAvatarUri: Uri? = null
    private var currentAvatarPath: String = ""

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedAvatarUri = it
                binding.ivAvatar.setImageURI(it)
                binding.ivAvatar.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener { finish() }

        val userViewModel = ViewModelProvider(this, UserViewModelFactory(applicationContext))[UserViewModel::class.java]

        userViewModel.currentUser.observe(this) { user ->
            user ?: return@observe
            if (binding.etName.text.isNullOrEmpty()) {
                binding.etName.setText(user.name)
            }
            if (binding.etBio.text.isNullOrEmpty()) {
                binding.etBio.setText(user.bio)
            }
            currentAvatarPath = user.avatar
            if (selectedAvatarUri == null) {
                val avatarFile = File(user.avatar)
                if (avatarFile.exists()) {
                    binding.ivAvatar.setImageURI(Uri.fromFile(avatarFile))
                }
            }
        }

        binding.ivAvatar.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val bio = binding.etBio.text.toString().trim()

            if (name.isEmpty()) {
                binding.etName.error = "El nombre no puede estar vacío"
                return@setOnClickListener
            }

            val avatarPath = selectedAvatarUri?.let { copyImageToInternal(it) } ?: currentAvatarPath

            userViewModel.currentUser.value?.let { user ->
                userViewModel.updateUser(user.copy(name = name, bio = bio, avatar = avatarPath))
            }
            finish()
        }
    }

    private fun copyImageToInternal(uri: Uri): String {
        val dir = File(filesDir, "images").also { it.mkdirs() }
        val file = File(dir, "${System.currentTimeMillis()}.jpg")
        contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        return file.absolutePath
    }
}