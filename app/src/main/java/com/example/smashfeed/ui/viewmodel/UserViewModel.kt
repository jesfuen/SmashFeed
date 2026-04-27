package com.example.smashfeed.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smashfeed.data.local.SmashFeedRoomDatabase
import com.example.smashfeed.data.model.User
import com.example.smashfeed.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(context: Context): ViewModel() {
    private val userRepository: UserRepository
    val users: LiveData<List<User>>

    private val _loginResult = MutableLiveData<User?>()
    val loginResult: LiveData<User?> get() = _loginResult

    init {
        val userDAO = SmashFeedRoomDatabase.getInstance(context).userDAO()
        userRepository = UserRepository(userDAO)
        users = userRepository.getAllUsers()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.loginOrRegister(username, password)
            _loginResult.postValue(user)
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUser(user)
        }
    }
}