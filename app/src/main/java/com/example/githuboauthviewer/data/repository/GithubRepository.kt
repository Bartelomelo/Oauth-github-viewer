package com.example.githuboauthviewer.data.repository

import com.example.githuboauthviewer.data.AppPreferences
import kotlinx.coroutines.flow.first

class GithubRepository(private val preferences: AppPreferences) {
    suspend fun saveToken(token: String) {
        preferences.saveToken(token)
    }

    suspend fun getToken(): String {
        return preferences.getToken.first()!!
    }
}