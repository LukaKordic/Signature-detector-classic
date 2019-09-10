package com.example.myapplication

import android.app.Application
import com.example.myapplication.di.appModule
import com.example.myapplication.di.interactionModule
import com.example.myapplication.di.networkingModule
import com.example.myapplication.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
  
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidContext(this@App)
      modules(listOf(appModule, presentationModule, networkingModule, interactionModule))
    }
  }
}