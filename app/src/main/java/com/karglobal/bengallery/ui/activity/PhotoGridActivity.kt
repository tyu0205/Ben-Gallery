package com.karglobal.bengallery.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.karglobal.bengallery.R
import com.karglobal.bengallery.viewmodel.PhotoViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class PhotoGridActivity : AppCompatActivity() {

    private val logTag = PhotoGridActivity::class.java.simpleName
    private val photoViewModel: PhotoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModelObserver();
        photoViewModel.downloadPhotos()
    }

    private fun initViewModelObserver() {
        photoViewModel.getPhotos().observe(this, Observer {
            Log.d(logTag, "Data size: " + it.size.toString())
        })


    }
}