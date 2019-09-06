package com.example.myapplication.content.interaction

import com.example.myapplication.networking.model.RepoResponse

interface GithubInteractor {
  
  fun fetchRepositories(
      user: String,
      onSuccess: (RepoResponse) -> Unit,
      onError: (Throwable) -> Unit
  )
}