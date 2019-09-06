package com.example.myapplication.content

import com.example.myapplication.networking.model.Repository

interface ContentContract {
  
  interface View {
    fun showRepositories(repos: List<Repository>)
    fun showError(error: String)
  }
  
  interface Presenter {
    fun setView(view: View)
    fun fetchRepositories()
  }
}