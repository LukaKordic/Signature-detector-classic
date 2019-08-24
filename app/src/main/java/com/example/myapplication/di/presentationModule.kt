package com.example.myapplication.di

import com.example.myapplication.welcome.WelcomeContract
import com.example.myapplication.welcome.presentation.WelcomePresenterImpl
import org.koin.dsl.module

val presentationModule = module {
  factory<WelcomeContract.WelcomePresenter> { WelcomePresenterImpl() }
}