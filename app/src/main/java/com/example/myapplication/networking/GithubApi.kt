package com.example.myapplication.networking

import com.example.myapplication.networking.model.RepoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {
  
  @GET("{user}/repos")
  fun getRepositories(@Path("user") user: String): Call<RepoResponse>
}