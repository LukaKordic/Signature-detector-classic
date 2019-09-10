package com.example.myapplication.content.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.networking.model.Repository

class RepoAdapter(private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<RepoViewHolder>() {
  private val repositories = mutableListOf<Repository>()
  
  fun setData(repos: List<Repository>) {
    repositories.clear()
    repositories.addAll(repos)
    notifyDataSetChanged()
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_item_layout, parent, false)
    return RepoViewHolder(view, onItemClick)
  }
  
  override fun getItemCount() = repositories.size
  
  override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
    holder.bind(repositories[position])
  }
}