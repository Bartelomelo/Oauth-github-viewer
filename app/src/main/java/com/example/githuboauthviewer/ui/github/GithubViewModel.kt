package com.example.githuboauthviewer.ui.github

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githuboauthviewer.data.AppPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class GithubViewModel(private val preferences: AppPreferences) : ViewModel() {

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    fun getToken() {
        _token.value = runBlocking {
            preferences.getToken.first()
        }
    }
}