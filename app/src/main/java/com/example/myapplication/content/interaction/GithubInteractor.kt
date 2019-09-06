package com.example.myapplication.content.interaction

import com.example.myapplication.networking.model.Repository

interface GithubInteractor {
  
  fun fetchRepositories(
      user: String,
      onSuccess: (List<Repository>) -> Unit,
      onError: (Throwable) -> Unit
  )
}