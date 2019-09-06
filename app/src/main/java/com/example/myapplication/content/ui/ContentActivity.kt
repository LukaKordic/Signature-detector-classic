package com.example.myapplication.content.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.content.ContentContract
import org.koin.android.ext.android.inject

class ContentActivity : AppCompatActivity(), ContentContract.View {
  
  private val presenter: ContentContract.Presenter by inject()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_content)
    
    presenter.setView(this)
    presenter.fetchRepositories()
  }
}