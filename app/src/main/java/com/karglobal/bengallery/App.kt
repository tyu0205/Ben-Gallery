package com.karglobal.bengallery

import android.app.Application
import com.karglobal.bengallery.di.APIModule
import com.karglobal.bengallery.di.PhotoViewModelModule
import com.karglobal.bengallery.di.RemoteRepositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            androidFileProperties()
            modules(
                listOf(
                    APIModule.apiModule,
                    PhotoViewModelModule.viewModelModule,
                    RemoteRepositoryModule.remoteRepositoryModule
                )
            )

        }
    }

}