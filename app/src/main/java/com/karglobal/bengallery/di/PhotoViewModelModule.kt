package com.karglobal.bengallery.di

import com.karglobal.bengallery.viewmodel.PhotoViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object PhotoViewModelModule {
    val viewModelModule = module {
        viewModel {
            PhotoViewModel(repository = get())
        }
    }
}