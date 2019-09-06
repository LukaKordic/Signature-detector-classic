package com.example.myapplication.content.interaction

import com.example.myapplication.networking.GithubApi
import com.example.myapplication.networking.model.RepoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubInteractorImpl(private val api: GithubApi) : GithubInteractor {
  override fun fetchRepositories(
      user: String,
      onSuccess: (RepoResponse) -> Unit,
      onError: (Throwable) -> Unit) {
    api.getRepositories(user).enqueue(object : Callback<RepoResponse> {
      override fun onFailure(call: Call<RepoResponse>, t: Throwable) {
        onError(t)
      }
      
      override fun onResponse(call: Call<RepoResponse>, response: Response<RepoResponse>) {
        if (response.isSuccessful) {
          response.body()?.run {
            onSuccess(this)
          }
        } else
          onError(Throwable(response.message()))
      }
    })
  }
}