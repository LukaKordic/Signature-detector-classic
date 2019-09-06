package com.example.myapplication.networking

import com.example.myapplication.networking.model.Repository
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {
  
  @GET("users/{user}/repos")
  fun getRepositories(@Path("user") user: String): Call<List<Repository>>
}