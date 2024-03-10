package com.example.githuboauthviewer.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githuboauthviewer.data.AppPreferences
import kotlinx.coroutines.launch

class AuthViewModel(private val preferences: AppPreferences): ViewModel() {
    fun saveToken(token: String) = viewModelScope.launch {
        preferences.saveToken(token)
    }
}