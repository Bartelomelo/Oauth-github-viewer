package com.example.githuboauthviewer.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githuboauthviewer.data.network.repository.GithubRepository
import com.example.githuboauthviewer.ui.auth.AuthViewModel
import com.example.githuboauthviewer.ui.github.GithubViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: GithubRepository
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository) as T
            modelClass.isAssignableFrom(GithubViewModel::class.java) -> GithubViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModelClass not found.")
        }
    }

}