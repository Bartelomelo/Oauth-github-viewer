package com.example.githuboauthviewer.ui.github

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githuboauthviewer.data.repository.GithubRepository
import kotlinx.coroutines.runBlocking

class GithubViewModel(private val repository: GithubRepository) : ViewModel() {

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    fun getToken() {
        _token.value = runBlocking {
            repository.getToken()
        }
    }
}