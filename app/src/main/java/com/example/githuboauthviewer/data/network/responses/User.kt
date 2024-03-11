package com.example.githuboauthviewer.data.network.responses

data class User(
    val login: String,
    val id: Int,
    val avatar_url: String,
    val type: String,
    val name: String,
    val public_repos: String,
    val created_at: String
)
