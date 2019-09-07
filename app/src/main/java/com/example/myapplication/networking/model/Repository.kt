package com.example.myapplication.networking.model

data class Repository(
    val name: String = "",
    val owner: RepoOwner
)

data class RepoOwner(
    val login: String,
    val avatar_url: String,
    val html_url: String
)