package com.example.githuboauthviewer.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githuboauthviewer.data.repository.GithubRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: GithubRepository): ViewModel() {
    fun saveToken(token: String) = viewModelScope.launch {
        repository.saveToken(token)
    }
}