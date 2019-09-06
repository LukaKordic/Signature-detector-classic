package com.example.myapplication.content.interaction

import com.example.myapplication.networking.GithubApi
import com.example.myapplication.networking.model.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubInteractorImpl(private val api: GithubApi) : GithubInteractor {
  override fun fetchRepositories(
      user: String,
      onSuccess: (List<Repository>) -> Unit,
      onError: (Throwable) -> Unit) {
    api.getRepositories(user).enqueue(object : Callback<List<Repository>> {
      override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
        onError(t)
      }
      
      override fun onResponse(call: Call<List<Repository>>, response: Response<List<Repository>>) {
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