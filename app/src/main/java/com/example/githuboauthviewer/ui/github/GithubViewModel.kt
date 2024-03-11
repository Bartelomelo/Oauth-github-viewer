package com.example.githuboauthviewer.ui.github

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githuboauthviewer.data.network.repository.GithubRepository
import com.example.githuboauthviewer.data.network.responses.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class GithubViewModel(private val repository: GithubRepository) : ViewModel() {

    private val _token = MutableLiveData<String>()
    private val _user = MutableLiveData<Response<User>>()
    val token: LiveData<String>
        get() = _token
    val user: LiveData<Response<User>>
        get() = _user

    fun getToken() {
        _token.value = runBlocking {
            repository.getToken()
        }
    }

    fun getUser(token: String) = viewModelScope.launch {
        _user.value = repository.getUser(token)
    }
}