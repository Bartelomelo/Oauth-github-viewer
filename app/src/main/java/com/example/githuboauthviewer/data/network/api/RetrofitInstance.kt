package com.example.githuboauthviewer.data.network.api

import com.example.githuboauthviewer.data.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: GithubApi by lazy {
        retrofit.create(GithubApi::class.java)
    }
}