package com.example.myapplication.di

import com.example.myapplication.content.interaction.GithubInteractor
import com.example.myapplication.content.interaction.GithubInteractorImpl
import org.koin.dsl.module

val interactionModule = module {
  factory<GithubInteractor> { GithubInteractorImpl(get()) }
}