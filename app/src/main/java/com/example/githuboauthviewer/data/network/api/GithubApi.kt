package com.example.githuboauthviewer.data.network.api

import com.example.githuboauthviewer.data.network.responses.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface GithubApi {
    @GET("/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<User>
}