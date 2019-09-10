package com.example.myapplication.di

import com.example.myapplication.common.ResourceManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
  single { ResourceManager(androidApplication()) }
}