package com.example.myapplication.content

interface ContentContract {
  
  interface View {
  
  }
  
  interface Presenter {
    fun setView(view: View)
    fun fetchRepositories()
  }
}