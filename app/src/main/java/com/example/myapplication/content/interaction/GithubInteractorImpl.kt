package com.example.myapplication.content.interaction

import com.example.myapplication.networking.GithubApi

class GithubInteractorImpl(private val api: GithubApi) : GithubInteractor {
  override fun fetchRepositories(user: String) {
    api.getRepositories(user)
  }
}