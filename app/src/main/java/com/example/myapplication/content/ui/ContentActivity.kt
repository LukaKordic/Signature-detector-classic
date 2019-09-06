package com.example.myapplication.content.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.content.ContentContract
import com.example.myapplication.content.ui.list.RepoAdapter
import com.example.myapplication.networking.model.Repository
import kotlinx.android.synthetic.main.activity_content.*
import org.koin.android.ext.android.inject

class ContentActivity : AppCompatActivity(), ContentContract.View {
  
  private val presenter: ContentContract.Presenter by inject()
  private val repoAdapter by lazy { RepoAdapter() }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_content)
    
    initList()
    presenter.setView(this)
    presenter.fetchRepositories()
  }
  
  override fun showRepositories(repos: List<Repository>) {
    repoAdapter.setData(repos)
  }
  
  override fun showError(error: String) {
    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
  }
  
  private fun initList() = with(repoList) {
    adapter = repoAdapter
    layoutManager = LinearLayoutManager(this@ContentActivity)
    setHasFixedSize(true)
  }
}