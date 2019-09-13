package com.example.myapplication.di

import com.example.myapplication.common.utils.ResourceManager
import com.example.myapplication.common.utils.Resources
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
  single<Resources> { ResourceManager(androidApplication()) }
}