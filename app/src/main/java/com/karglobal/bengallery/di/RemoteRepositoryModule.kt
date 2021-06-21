package com.karglobal.bengallery.di

import android.content.Context
import com.karglobal.bengallery.api.UnsplashAPI
import com.karglobal.bengallery.repository.RemoteRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Here is the module for creating a remote repository
 */
object RemoteRepositoryModule {
    val remoteRepositoryModule = module {

        fun provideRemoteRepository(api: UnsplashAPI, context: Context): RemoteRepository {
            return RemoteRepository(api, context)
        }
        single { provideRemoteRepository(get(), androidContext()) }
    }
}