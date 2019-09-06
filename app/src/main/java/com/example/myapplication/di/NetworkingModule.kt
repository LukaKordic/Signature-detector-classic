package com.example.myapplication.di

import com.example.myapplication.BuildConfig
import com.example.myapplication.networking.GithubApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.github.com/"

val networkingModule = module {
  single { GsonConverterFactory.create() as Converter.Factory }
  
  //interceptors
  single {
    HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY) as HttpLoggingInterceptor
  }
  single {
    Interceptor { chain ->
      val authentication = chain.request().newBuilder().apply {
      
      }.build()
      chain.proceed(authentication)
    }
  }
  
  //okHttpClient
  single {
    OkHttpClient.Builder().apply {
      if (BuildConfig.DEBUG) addInterceptor(get<HttpLoggingInterceptor>())
      readTimeout(1L, TimeUnit.MINUTES)
      connectTimeout(1L, TimeUnit.MINUTES)
    }.build()
  }
  
  //retrofit
  single {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(get())
        .addConverterFactory(get())
        .build()
  }
  
  single { get<Retrofit>().create(GithubApi::class.java) }
}