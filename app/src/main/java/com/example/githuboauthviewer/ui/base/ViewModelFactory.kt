package com.example.githuboauthviewer.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githuboauthviewer.data.AppPreferences
import com.example.githuboauthviewer.ui.auth.AuthViewModel
import com.example.githuboauthviewer.ui.github.GithubViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val preferences: AppPreferences
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(preferences) as T
            modelClass.isAssignableFrom(GithubViewModel::class.java) -> GithubViewModel(preferences) as T
            else -> throw IllegalArgumentException("ViewModelClass not found.")
        }
    }

}