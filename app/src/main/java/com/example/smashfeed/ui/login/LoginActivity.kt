package com.example.smashfeed.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.smashfeed.R
import com.example.smashfeed.databinding.ActivityLoginBinding
import com.example.smashfeed.ui.feed.FeedActivity
import com.example.smashfeed.ui.viewmodel.UserViewModel
import com.example.smashfeed.ui.viewmodel.UserViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isLoggedIn()) {
            goToFeed()
            return
        }

        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userViewModel = ViewModelProvider(this, UserViewModelFactory(applicationContext))[UserViewModel::class.java]

        userViewModel.loginResult.observe(this) { user ->
            if (user != null) {
                saveSession(user.id!!, user.username)
                goToFeed()
            } else {
                binding.editPassword.error = "Usuario o contraseña incorrectos"
            }
        }

        binding.buttonLogin.setOnClickListener {
            val username = binding.editUsername.text.toString().trim()
            val password = binding.editPassword.text.toString()
            userViewModel.login(username, password)
        }
    }

    private fun saveSession(userId: Int, username: String) {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
        }
    }

    private fun isLoggedIn(): Boolean {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.contains(KEY_USERNAME) && prefs.contains(KEY_USER_ID)
    }

    private fun goToFeed() {
        startActivity(Intent(this, FeedActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    companion object {
        const val PREFS_NAME = "smashfeed_prefs"
        const val KEY_USER_ID = "user_id"
        const val KEY_USERNAME = "username"
    }
}