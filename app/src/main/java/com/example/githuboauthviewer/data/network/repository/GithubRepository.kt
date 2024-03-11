package com.example.githuboauthviewer.data.network.repository

import com.example.githuboauthviewer.data.network.api.RetrofitInstance
import com.example.githuboauthviewer.data.network.responses.User
import com.example.githuboauthviewer.data.utils.AppPreferences
import kotlinx.coroutines.flow.first
import retrofit2.Response

class GithubRepository(private val preferences: AppPreferences) {
    suspend fun saveToken(token: String) {
        preferences.saveToken(token)
    }

    suspend fun getToken(): String {
        return preferences.getToken.first()!!
    }

    suspend fun getUser(token: String): Response<User> {
        return RetrofitInstance.api.getUser(token)
    }
}