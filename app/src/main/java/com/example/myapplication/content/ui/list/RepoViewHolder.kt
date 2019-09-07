package com.example.myapplication.content.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.networking.model.Repository
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.repo_item_layout.view.*

class RepoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
  
  fun bind(data: Repository) = with(containerView) {
    repoName.text = data.name
    userName.text = data.owner.login
    Glide.with(this).load(data.owner.avatar_url).into(userImage)
  }
}