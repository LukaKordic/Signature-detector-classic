package com.example.myapplication.content.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.networking.model.Repository
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.repo_item_layout.view.*

class RepoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
  
  fun bind(data: Repository) = with(containerView) {
    Glide.with(this).load(data.image).into(repoImage)
    repoName.text = data.name
  }
}