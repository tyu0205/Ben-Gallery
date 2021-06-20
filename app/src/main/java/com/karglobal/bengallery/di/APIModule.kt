package com.karglobal.bengallery.di

import com.karglobal.bengallery.BuildConfig
import com.karglobal.bengallery.api.UnsplashAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIModule {

    val apiModule = module {

        fun httpInterceptor() = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        fun basicOkHttpClient() = OkHttpClient.Builder().addInterceptor(httpInterceptor()).build()


        fun provideUnsplashAPIService(): UnsplashAPI {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create()).client(basicOkHttpClient())
                .baseUrl(BuildConfig.UNSPLASH_URL)
                .build()
            return retrofit.create(UnsplashAPI::class.java);
        }

        single { provideUnsplashAPIService() }

    }

}