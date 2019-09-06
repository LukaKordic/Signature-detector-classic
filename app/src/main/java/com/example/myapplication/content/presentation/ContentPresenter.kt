package com.example.myapplication.content.presentation

import com.example.myapplication.common.DEFAULT_USER_NAME
import com.example.myapplication.content.ContentContract
import com.example.myapplication.content.interaction.GithubInteractor
import com.example.myapplication.networking.model.Repository

class ContentPresenter(private val interactor: GithubInteractor) : ContentContract.Presenter {
  private lateinit var view: ContentContract.View
  
  override fun setView(view: ContentContract.View) {
    this.view = view
  }
  
  override fun fetchRepositories() {
    interactor.fetchRepositories(DEFAULT_USER_NAME, ::onReposReceived, ::onError)
  }
  
  private fun onReposReceived(response: List<Repository>) {
    view.showRepositories(response)
  }
  
  private fun onError(error: Throwable) {
    view.showError(error.message ?: "")
  }
}